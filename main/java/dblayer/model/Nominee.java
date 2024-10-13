package dblayer.model;

public class Nominee {
	
    private Long userID;
    private String name;
    private String relationship;

    public Nominee() {}
    
    public Long getUserId() {
        return userID;
    }

    public void setUserID(Long userID) {
        this.userID = userID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelationship() {
        return relationship;
    }

    public void setRelationship(String relationship) {
        this.relationship = relationship;
    }

    @Override
    public String toString() {
        return "Nominee{" +
                "id=" + userID +
                ", name='" + name + '\'' +
                ", relationship ='" + relationship + '\'' + '}';
    }
}
