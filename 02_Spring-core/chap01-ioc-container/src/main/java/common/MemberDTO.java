package common;

public class MemberDTO {

    private int sequence;
    private String id;
    private String pw;
    private String name;

    public MemberDTO() {
    }

    public MemberDTO(int sequence, String id, String pw, String name) {
        this.sequence = sequence;
        this.id = id;
        this.pw = pw;
        this.name = name;
    }

    public int getSequence() {
        return sequence;
    }

    public void setSequence(int sequence) {
        this.sequence = sequence;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPw() {
        return pw;
    }

    public void setPw(String pw) {
        this.pw = pw;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "MemberDTO{" +
                "sequence=" + sequence +
                ", id='" + id + '\'' +
                ", pw='" + pw + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
