module cse360prototype {
    requires transitive javafx.base;
    requires transitive javafx.controls;
    requires transitive javafx.graphics;

    opens prototype to javafx.graphics;
    exports prototype;
}