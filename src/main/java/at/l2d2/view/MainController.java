package at.l2d2.view;

import at.l2d2.entity.Product;
import at.l2d2.service.PriceService;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.controlsfx.dialog.ProgressDialog;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {
	@FXML private Button     btnRefresh;
	@FXML private ScrollPane scrollPane;
	@FXML private Label      lblTitel;
	@FXML private BorderPane anchor;

	@FXML private TableView<Product>           priceTable;
	@FXML private TableColumn<Product, String> colASIN;
	@FXML private TableColumn<Product, String> colName;
	@FXML private TableColumn<Product, String> colOrigPr;
	@FXML private TableColumn<Product, String> colDelivPr;
	@FXML private TableColumn<Product, String> colDif;

	private PriceService priceService = new PriceService();
	private Stage stage;
	private FileChooser fileChooser = new FileChooser();
	private File file;

	@FXML
	public void onClickRefresh(ActionEvent event) {
		priceTable.getItems().clear();
		loadData();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		colASIN.setCellValueFactory(cellData -> cellData.getValue().ASINProperty());
		colName.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		colOrigPr.setCellValueFactory(cellData -> cellData.getValue().originalPriceProperty());
		colDelivPr.setCellValueFactory(cellData -> cellData.getValue().deliveredPriceProperty());
		colDif.setCellValueFactory(cellData -> cellData.getValue().differenceProperty());

		colDif.setCellFactory(col -> new TableCell<Product, String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);

				if (item == null || empty) {
					setText(null);
					setStyle("");
				} else {
					setText(item);
					// Style all dates in March with a different color.
					if (item.equals("0.0") || item.equals("0") || item.equals("")) {
						setTextFill(Color.BLACK);
						setStyle("");
					} else {
						setTextFill(Color.RED);
						setStyle("-fx-background-color: yellow");
					}
				}
			}
		});

		loadData();
	}

	private File showPicker() {
		fileChooser.setTitle("Produkte wählen...");
		fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
		fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV", "*.csv"));
		return fileChooser.showOpenDialog(stage);
	}

	private void loadData() {
		Service<Void> service = new Service<Void>() {
			@Override
			protected Task<Void> createTask() {
				return new Task<Void>() {
					@Override
					protected Void call() throws InterruptedException {
						List<String[]> lines = priceService.readFile();
						List<Product> products = new ArrayList<>();

						updateMessage("Lade Daten . . .");
						updateProgress(0, lines.size());

						int i = 0;
						for (String[] line : lines) {
							updateProgress(i + 1, lines.size());
							updateMessage("Lade " + (i + 1) + "/" + lines.size() + " Daten!");
							products.add(priceService.loadProduct(line[1], line[0], line[2]));
							i++;
						}
						updateMessage("Fertig!");
						ObservableList<Product> data = FXCollections.observableArrayList(products);
						priceTable.getItems().addAll(data);
						return null;
					}
				};
			}
		};

		ProgressDialog progDiag = new ProgressDialog(service);
		progDiag.setTitle("Lade Daten");
		progDiag.initOwner(stage);
		progDiag.setHeaderText("Lade Daten");
		progDiag.initModality(Modality.WINDOW_MODAL);
		service.start();
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}
}
