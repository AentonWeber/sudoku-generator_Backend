package at.codersbay.springboot.learning;

public class Photo {

    private String id;
    private String fileName;

    // raw data


    public Photo() {
    }

    public Photo(String id, String fileName) {
        this.id = id;
        this.fileName = fileName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
