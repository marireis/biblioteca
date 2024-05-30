package com.marina.bibilioteca.principal;

import com.marina.bibilioteca.model.Autor;
import com.marina.bibilioteca.model.Livro;
import com.marina.bibilioteca.model.dto.LivroDTO;
import com.marina.bibilioteca.repository.LivroRepository;
import com.marina.bibilioteca.service.ConsumoApi;
import com.marina.bibilioteca.service.ConverteDados;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class Principal {
    private ConsumoApi consumoApi = new ConsumoApi();
    private ConverteDados converteDados = new ConverteDados();
    private Scanner leitura = new Scanner(System.in);
    private LivroRepository repository;
    private String ENDERECO = "https://gutendex.com/books/?search=";

    public Principal(LivroRepository repository) {
        this.repository = repository;
    }

    public void exibeMenu(){
        int opcao = -1;
        while (opcao != 0){
            String menu = """
                \n-------------------------------------------
                  *********** MENU BIBLIOTECA ***********
                -------------------------------------------
                1 - Buscar livro por título
                2 - Listar livros registrados
                3 - Listar Autores
                4 - Listar Autores vivos em determinado ano
                5 - Listar Livros em determinado Idioma
                6 - Exibir a quantidade de livros em um determinado idioma.
                0 - Sair
                -------------------------------------------
                """;
            try {
                System.out.println(menu);
                opcao = leitura.nextInt();
                leitura.nextLine();

                switch (opcao){
                    case 1:
                        buscarLivro();
                        break;
                    case 2:
                        listarLivros();
                        break;
                    case 3:
                        listarAutores();
                        break;
                    case 4:
                        listarAutoresVivosNoAno();
                        break;
                    case 5:
                        listarLivrosPorIdioma();
                        break;
                    case 6:
                        quantidadeLivrosPorIdioma();
                        break;
                    case 0:
                        System.out.println("Saindo...");
                        break;
                    default:
                        System.out.println("Opção inválida");
                }
            }catch (InputMismatchException e){
                System.out.println("Entrada inválida. Insira um número inteiro.");
                leitura.nextLine();
            }
        }
    }

    private void buscarLivro() {
        System.out.println("Digite o nome do Livro: ");
        var nomeLivro = leitura.nextLine();

        String busca = ENDERECO.concat(nomeLivro.replace(" ", "+").toLowerCase().trim());

        String json = consumoApi.buscar(busca);
        String jsonLivro = converteDados.extraiObjetoJson(json, "results");

        List<LivroDTO> livrosDTO = converteDados.obterLista(jsonLivro, LivroDTO.class);

        if (!livrosDTO.isEmpty()) {
            Livro livro = new Livro(livrosDTO.get(0));

            //Verifica se o Autor já está cadastrado
            Autor autor = repository.buscarAutorPeloNome(livro.getAutor().getAutor());
            if (autor != null) {
                livro.setAutor(null);
                repository.save(livro);
                livro.setAutor(autor);
            }
            livro = repository.save(livro);
            System.out.println(livro);
        } else {
            System.out.println("Livro não encontrado");
        }
    }

    private void listarLivros() {
    }

    private void listarAutores() {
    }

    private void listarAutoresVivosNoAno() {
    }
    private void listarLivrosPorIdioma() {
    }

    private void quantidadeLivrosPorIdioma() {
    }

}
