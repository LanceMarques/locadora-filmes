package com.locadora.infra.locacao;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.locadora.infra.cliente.Cliente;
import com.locadora.infra.cliente.ClienteService;
import com.locadora.infra.enums.StatusLocacao;
import com.locadora.infra.filme.Filme;
import com.locadora.infra.locacao.exceptions.LocacaoLimiteDeFilmesException;
import com.locadora.infra.locacao.exceptions.LocacaoNaoEncontradaException;
import com.locadora.infra.locacaoTemFilme.LocacaoTemFilme;
import com.locadora.infra.locacaoTemFilme.LocacaoTemFilmeService;
import com.locadora.utils.DataUtils;

/**
 * Classe responsavel por implementar as funcionalidades e validacoes
 * solicitadas pelo controller da entidade Locacao
 * 
 * @version 1.0.0 Abril/2019
 * @author Luis Lancellote
 * @since 1.0.0
 */
@Service
public class LocacaoService {

	@Autowired
	private LocacaoRepository locacaoRepository;
	@Autowired
	private ClienteService clienteService;
	@Autowired
	private LocacaoTemFilmeService locacaoTemFilmeService;

	/**
	 * Metodo responsavel por fornecer uma lista com todas as {@link Locacao
	 * locacoes} cadastradas no sistema que se adequam ao filtro recebido como
	 * parametro.
	 * 
	 * @param filter {@link LocacaoFilter} Filtro aplicado na consulta de locacoes
	 * @return Lista com todas as {@link Locacao locacoes} encontradas na pesquisa
	 */
	public List<Locacao> pesquisar(LocacaoFilter filter) {
		return this.locacaoRepository.filtrar(filter);
	}

	/**
	 * Metodo responsavel por fornecer uma {@link Locacao locacao} que possui o id
	 * informado.
	 * 
	 * @param id ({@link Integer}) Id requisitado na pesquisa
	 * @return Filme{@link Filme}} que possui o id informado.
	 * 
	 * @since 1.0.0
	 */
	public Locacao buscarPorId(Integer id) {
		final Optional<Locacao> locacaoOpt = locacaoRepository.findById(id);
		if (!locacaoOpt.isPresent()) {
			throw new LocacaoNaoEncontradaException();
		}
		return locacaoOpt.get();
	}

	/**
	 * Metodo responsavel por fornecer uma lista com todas as {@link Locacao
	 * locacoes} em aberto do cliente que possui o cpf recebido como parametro.
	 * 
	 * @param cpf {@link String} Cpf requisitado na pesquisa
	 * @return locacoes {@link Locacao} Lista com todas as {@link Locacao locacoes}
	 *         encontradas na pesquisa.
	 */
	public List<Locacao> listarPorCPF(String cpf) {
		final Cliente clienteSalvo = this.clienteService.buscarClientePorCpf(cpf);
		final List<Locacao> locacoes = this.listarPendenciasDoCliente(clienteSalvo);
		return locacoes;
	}

	/**
	 * Metodo responsavel por fornecer uma lista com todas os {@link LocacaoTemFilme
	 * filmes} de uma locacao que possua o id recebido como parametro.
	 * 
	 * @param id {@link Integer} Id da locacao requisitado na pesquisa.
	 * @return {@link List Lista} com todos os {@link LocacaoTemFilme filmes} de uma
	 *         locacao.
	 */
	public List<LocacaoTemFilme> listarFilmes(Integer id) {
		Locacao locacaoSalva = this.buscarPorId(id);
		return locacaoSalva.getFilmes();
	}

	/**
	 * Metodo responsavel por cadastrar uma locacao recebida como parametro
	 * 
	 * @param locacao {@link Locacao} locacao recebido como parametro para cadastro
	 *                no sistema
	 * @return {@link Locacao locacao} Locacao cadastrada no sistema.
	 * 
	 * @since 1.0.0
	 */
	public Locacao criar(Locacao locacao) {
		final Locacao locacaoSalva = locarFilmes(locacao);
		return this.locacaoRepository.save(locacaoSalva);
	}

	/**
	 * Metodo responsavel por atualizar uma {@link Locacao locacao} que possui o id
	 * solicitado com os dados recebidos no corpo da requisicao.
	 * 
	 * @param id      ({@link Integer}) Id da locacao a ser atualizada.
	 * @param locacao {@link Locacao} Dados atualizados.
	 * 
	 * @since 1.0.0
	 */
	public Locacao atualizar(Integer id, Locacao locacao) {
		final Locacao locacaoSalva = buscarPorId(id);
		BeanUtils.copyProperties(locacao, locacaoSalva, "id");
		return locacaoRepository.save(locacaoSalva);
	}

	/**
	 * Metodo responsavel por efetivar o processo de devolucao de uma {@link Locacao
	 * locacao} que possui o id recebido como parametro.
	 * 
	 * @param id ({@link Integer}) Id da locacao a ser devolvida.
	 * 
	 * @since 1.0.0
	 */
	public void devolverLocacao(Integer id) {
		final Locacao locacaoSalva = this.buscarPorId(id);
		this.devolverFilmes(locacaoSalva);
		locacaoSalva.setDataDevolucao(DataUtils.gerarDataAtual());
		locacaoSalva.setStatus(StatusLocacao.FINALIZADO);
		locacaoSalva.setValorTotal(calcularValorTotal(locacaoSalva));
		this.locacaoRepository.save(locacaoSalva);
	}

	/**
	 * Metodo responsavel por calcular o valor total de uma {@link Locacao locacao}
	 * recebida como parametro
	 * 
	 * @param locacao {@link Locacao} Dados da locacao para o calculo do valor
	 *                total.
	 * @return valorTotal Valor total calculado.
	 * @since 1.0.0
	 */
	public Double calcularValorTotal(Locacao locacao) {
		final Long intervaloMilis = locacao.getDataDevolucao().getTime() - locacao.getDataRealizacao().getTime();
		final Long intervaloDias = 1 + intervaloMilis / (1000 * 60 * 60 * 24);
		double totalFilmes = 0;
		for (LocacaoTemFilme locacaoTemfilme : locacao.getFilmes()) {
			totalFilmes += locacaoTemfilme.getValorTotalDaDiaria();
		}
		final Double valorTotal = totalFilmes * intervaloDias;
		return valorTotal;
	}

	/**
	 * Metodo responsavel por excluir uma {@link Locacao locacao} que possui o id
	 * informado.
	 * 
	 * @param id ({@link Integer}) Id da Locacao a ser excluida.
	 * 
	 * @since 1.0.0
	 */
	public void excluir(Integer id) {
		final Locacao locacaoSalva = this.buscarPorId(id);
		devolverFilmes(locacaoSalva);
		this.locacaoRepository.deleteById(locacaoSalva.getId());
	}

	/**
	 * Metodo responsavel por validar e inicializar os dados da locacao a ser
	 * cadastrada.
	 * 
	 * @param locacao ({@link Locacao}) locacao a ser validada e inicializada.
	 * 
	 * @since 1.0.0
	 */
	private Locacao locarFilmes(Locacao locacao) {

		List<LocacaoTemFilme> filmesLocados = this.locacaoTemFilmeService.verificaFilmes(locacao.getFilmes());
		final Cliente clienteValido = buscarClienteValido(locacao);

		locacao.setFilmes(null);
		locacao.setCliente(clienteValido);
		locacao.setStatus(StatusLocacao.ABERTO);
		locacao.setDataRealizacao(DataUtils.gerarDataAtual());

		Locacao locacaoSalva = this.locacaoRepository.save(locacao);

		filmesLocados = locacaoTemFilmeService.associarFilmes(filmesLocados, locacaoSalva);

		locacaoSalva.setFilmes(filmesLocados);
		return locacaoSalva;

	}

	/**
	 * Metodo responsavel por atualizar o estoque dos filmes de uma locacao
	 * devolvida recebida como parametro
	 * 
	 * @param locacaoDevolvida ({@link Locacao}) Locacao a ser devolvida
	 * 
	 * @since 1.0.0
	 */
	private void devolverFilmes(Locacao locacaoDevolvida) {
		final List<LocacaoTemFilme> filmesDevolvidos = locacaoDevolvida.getFilmes();
		this.locacaoTemFilmeService.devolverAoEstoque(filmesDevolvidos);
	}

	/**
	 * Metodo responsavel por fornecer uma lista que tenha apenas as {@link Locacao
	 * locacoes} em aberto do cliente que possui o cpf recebido como parametro
	 * 
	 * @param cpf {@link String} Cpf requisitado na pesquisa
	 * @return locacoes {@link Locacao} Lista com todas as {@link Locacao locacoes}
	 *         encontradas na pesquisa
	 */
	private List<Locacao> listarPendenciasDoCliente(Cliente clienteSalvo) {
		final List<Locacao> locacoes = this.locacaoRepository.findByCliente(clienteSalvo);
		locacoes.removeIf(locacao -> locacao.getStatus() == StatusLocacao.FINALIZADO);
		return locacoes;
	}

	private Cliente buscarClienteValido(Locacao locacao) {
		final Integer clienteId = locacao.getCliente().getId();

		final Integer filmesNaLocacao;
		final Integer filmesComCliente;

		final Cliente clienteSalvo = clienteService.buscarPorId(clienteId);
		final List<Locacao> locacoesDoCliente = this.listarPendenciasDoCliente(clienteSalvo);

		filmesNaLocacao = locacaoTemFilmeService.contarFilmes(locacao.getFilmes());
		filmesComCliente = locacaoTemFilmeService.contarFilmesNasLocacoes(locacoesDoCliente);

		if (filmesComCliente + filmesNaLocacao > 5) {
			throw new LocacaoLimiteDeFilmesException();
		}
		return clienteSalvo;
	}

}
