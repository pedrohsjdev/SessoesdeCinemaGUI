package com.cinemagui;

/*
 * @Authors: Gustavo Silva, Iago Rocha, Letícia Suga
 *           Pedro Silva, Rafael Pereira, Walison Santana.
 *
 * @Theme: Gerenciamento de sessões de cinema.
 *
 * @UserInterface: Interface Ǵráfica.
 *
 * @Library: JavaFX.
 *
*/

/*
 * Por convenção, costuma-se trabalhar com propriedas no JavaFX, ao invés
 * de atributos "primitivos". As propriedades permitem uma integração mais 
 * robusta com outras classes do Kit de Ferramentas JavaFX. Por exemplo,
 * é possível detectar facilmente quando o valor de uma propriedade for
 * modificada e, assim, atualizar os dados que o usuário está visualizando.
 *
 * Em vista disso, foram utilizadas propriedades nas principais classes do
 * projeto (Filme, Sessao, Sala e Cinema).
 *
*/

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.text.Font;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import javafx.stage.Modality;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/* CICLO DE VIDA DE UMA APLICAÇÃO JAVAFX
 * 
 * 1 - Cria uma instância da classe filha da classe Application. 
 * 1 - Chama o método init().
 * 3 - Chama o método start(Stage).
 * 4 - Espera a aplicação finalizar. Isso pode acontecer de duas formas:
 *   -> A aplicação chama Platform.exit().
 *   -> A última janela é fechada e o atributo implicitExit da Platform é true.
 * 5 - Chama o método stop().
 *
*/
	
//Toda aplicação JavaFX precisa estender a classe Application.
public class CinemaUtil extends Application{

	//Stage para perguntar o nome do cinema.
	private static Stage stageGetCinemaName = new Stage();

	//Esse objeto será utilizada pelos métodos "Start()" e "enterCinemaName()".
	private static ControllerMain controllerMain = null;

	@FXML
	//Input para coletar o nome do cinema.
	private TextField inputCinemaName;
	
	/* 
	 * O método main é opcional em uma aplicação JavaFx.
	 *
	 *	public static void main(String[] args) throws Exception{
	 *		Application.launch(args);
	 *	}
	 *
	 * A explicação, com detalhes, pode ser encontrada aqui: https://bit.ly/3DlU8ZT.
	 * Mas, de forma simples, se a classe main for uma subclasse da classe Application,
	 * o Java usa sua própria classe main interna para carregar o toolkit do JavaFX,
	 * enquanto que essa classe main (CinemaUtil) só será chamada, posteriormente, pelo JavaFX.
	 * Então, se não tiver uma classe main, o JavaFX fica responsável por lançar a aplicação
	 * através de um método interno.
	 *
	*/
	
	@Override
	public void init() throws Exception {

		//Carregando as fontes necessárias.
		Font.loadFont(getClass().getResource("resources/fonts/Montserrat-Light.ttf").toString(), 300);
		Font.loadFont(getClass().getResource("resources/fonts/Montserrat-Medium.ttf").toString(), 500);
		Font.loadFont(getClass().getResource("resources/fonts/Montserrat-MediumItalic.ttf").toString(), 500);
		Font.loadFont(getClass().getResource("resources/fonts/Montserrat-SemiBold.ttf").toString(), 600); 
		Font.loadFont(getClass().getResource("resources/fonts/Montserrat-Bold.ttf").toString(), 700); 
		Font.loadFont(getClass().getResource("resources/fonts/Montserrat-Black.ttf").toString(), 900);

		//Impede que o usuário consiga redimensionar a janela.
		stageGetCinemaName.setResizable(false);
		
		//Chama o método para deserializar os dados armazenados.
		Cinema.unserializeData();

	}

	@Override
	public void start(Stage stageMain) throws Exception {

		//Carrega a SceneGraph principal.
		FXMLLoader loader = new FXMLLoader(getClass().getResource("SceneGraphMain.fxml"));

		//Carrega o nó raiz da SceneGraph principal.
		Parent rootNodeSceneMain = (Parent) loader.load();

		//Carregar a classe controladora da SceneGraph principal para a variável especificada.
		controllerMain = loader.getController();

		//Carrega o nó raiz da SceneGraph responsável por perguntar o nome do cinema.
		Parent rootNodeGetCinemaName = FXMLLoader.load(getClass().getResource("SceneGraphGetCinemaName.fxml"));
			
		//A partir dos SceneGraph carregados anteriormente, cria as Scenes utilizando o nó raiz.
		Scene sceneMain = new Scene(rootNodeSceneMain);
		Scene sceneGetCinemaName = new Scene(rootNodeGetCinemaName);
		
		//Coloca as Scenes dentro dos stages.
		stageMain.setScene(sceneMain);
		stageGetCinemaName.setScene(sceneGetCinemaName);

		try {
			//Carrega o arquivo que sinaliza o nome do cinema.
			System.out.println(new FileReader("cinemaName.flag"));
			FileReader readerFlagCinemaName = new FileReader("cinemaName.flag");

			/*
			 * Verifica o valor da flag. Se for 0, então o usuário ainda não definiu um
			 * nome para o cinema, então o programa abre a Stage para perguntar o nome.
			 *
			*/

			if( (char) readerFlagCinemaName.read() == '0') {

				/*
				 * o método "ShowAndWait()" só permite que as próximas linhas sejam
				 * executadas se a Stage for finalizada.
				 *
				*/

				stageGetCinemaName.showAndWait();
			} else {

				String cinemaName = "";
				while (readerFlagCinemaName.ready()) {
					cinemaName += (char) readerFlagCinemaName.read();
				}

				controllerMain.setName(cinemaName);

			}


			readerFlagCinemaName.close();
		} catch (IOException e) {
			System.out.println("Flag not found!");
		}
		
		//Impede que o usuário consiga redimensionar a janela.
		stageMain.setResizable(false);
		
		//Abre a Stage principal
		stageMain.show();
	}

	/*
	 * A notação "@FXML" indica que o método pode ser chamado 
	 * pelo arquivo FXML que possui essa classe como controlador,
	 * independentemente, do modificador do método ser private.
	 *
	*/

	@FXML
	private void enterCinemaName() {

		//Verifica se o input está vazio.
		if(!inputCinemaName.getText().isEmpty()){

			//Se não estiver, define o texto para ser o nome do cinema.
			controllerMain.setName(inputCinemaName.getText());

			//Encerra o Stage.
			stageGetCinemaName.close();

			//Modifica o sinalizador para 1 e salva o nome do cinema para a próxima abertura.
			try {

				FileWriter writerFlagCinemaName = new FileWriter("cinemaName.flag");

				writerFlagCinemaName.write("1");
				writerFlagCinemaName.write(inputCinemaName.getText());
				System.out.println(Cinema.getName());
				System.out.println(inputCinemaName.getText());

				writerFlagCinemaName.close();

			} catch (IOException e) {
				System.out.println("It's not possible write in the flag file!");
			}

		} else {

			//Cria um alerta para informar o usuário que o input está vazio
			Alert a = new Alert(AlertType.INFORMATION, "Ops! Parece que você não digitou o nome do cinema.");
			a.showAndWait();
		}
	}	

	public void stop() throws Exception {
		try {
			//Chama o método para serializar os dados
			Cinema.serializeData();
		} catch (IOException e) {
			System.out.println("Serialize file not found, aborting serializaton.");
		}
	}


	

}
