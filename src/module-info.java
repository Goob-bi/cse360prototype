module cse360prototype {
    requires transitive javafx.base;
    requires transitive javafx.controls;
    requires transitive javafx.graphics;

    opens prototype to javafx.graphics;
    exports prototype;
    exports prototype.menus;
    opens prototype.menus to javafx.graphics;
    exports prototype.data;
    opens prototype.data to javafx.graphics;
    exports prototype.obj;
    opens prototype.obj to javafx.graphics;
    exports org.json;
}