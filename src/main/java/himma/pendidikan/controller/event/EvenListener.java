package himma.pendidikan.controller.event;

import javafx.event.ActionEvent;
import himma.pendidikan.controller.KaryawanCtrl;

public abstract class EvenListener {
    private static KaryawanCtrl kry = new KaryawanCtrl();

    public abstract void handleClear();


    public static abstract class EvenListenerIndex extends EvenListener {
        public abstract void handleSearch();
    }


    public static abstract class EvenListenerCreate extends EvenListener{
        public abstract void handleAddData(ActionEvent e);

        public void handleBack(){
            kry.loadSubPage("index",null);
        };
    }


    public static abstract class EvenListenerUpdate extends EvenListener{
        public abstract void handleUpdateData(ActionEvent e);

        public abstract void loadData();

        public void handleBack(){
            kry.loadSubPage("index",null);
        };
    }
}
