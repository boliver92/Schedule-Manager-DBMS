package views.resources.styles;

public enum Colors {
    WARNING ("#d63031"),
    SUCCESS ("#00b894");

    private final String name;

    private Colors(String s){
        name = s;
    }

    public  String toString(){
        return this.name;
    }
}
