package pe.edu.upeu.comidata.components;

import javafx.geometry.Side;
import javafx.scene.control.TextField;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import pe.edu.upeu.comidata.dto.ModeloDataAutocomplet;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Autocompletado {

    public static void autocompletarTextField(TextField textField, List<ModeloDataAutocomplet> dataList, Consumer<ModeloDataAutocomplet> onSelect) {
        ContextMenu contextMenu = new ContextMenu();
        textField.setContextMenu(contextMenu);

        textField.textProperty().addListener((obs, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                contextMenu.hide();
                return;
            }

            List<ModeloDataAutocomplet> filteredList = dataList.stream()
                    .filter(data -> data.getLabel().toLowerCase().contains(newValue.toLowerCase()) ||
                            data.getSearchKey().toLowerCase().contains(newValue.toLowerCase()))
                    .collect(Collectors.toList());

            contextMenu.getItems().clear();

            for (ModeloDataAutocomplet data : filteredList) {
                MenuItem item = new MenuItem(data.getLabel());
                item.setOnAction(event -> {
                    textField.setText(data.getLabel());
                    onSelect.accept(data);
                    contextMenu.hide();
                });
                contextMenu.getItems().add(item);
            }

            if (!contextMenu.isShowing() && !filteredList.isEmpty()) {
                contextMenu.show(textField, Side.BOTTOM, 0, 0);
            } else if (filteredList.isEmpty()) {
                contextMenu.hide();
            }
        });

        textField.focusedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                contextMenu.hide();
            }
        });
    }
}