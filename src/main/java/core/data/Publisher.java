package core.data;

public class Publisher {

    private String name;
    private int id;

    public Publisher( String name ) {
        this.name = name;
    }

    public Publisher() {

    }

    public String getName() {
        return name;
    }

    public void setName( String name ) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId( int id ) {
        this.id = id;
    }

    @Override
    public String toString() {
        return this.getName();
    }

}
