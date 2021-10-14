package notyet.newword.model;

public class BoardData {
    private String word; //신조어
    private String mean;
    private String wirter; //작성자
    private String time; //작성시간
    private int n_like; //좋아요 수
    private String uid;
    public BoardData(String word, String mean,String wirter, String time, int n_like,String uid) {
        this.word = word;
        this.mean=mean;
        this.wirter = wirter;
        this.time = time;
        this.n_like = n_like;
        this.uid=uid;
     }

    public BoardData() {
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getMean() {
        return mean;
    }

    public void setMean(String mean) {
        this.mean = mean;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public String getWirter() {
        return wirter;
    }

    public void setWirter(String wirter) {
        this.wirter = wirter;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getN_like() {
        return n_like;
    }

    public void setN_like(int n_like) {
        this.n_like = n_like;
    }


}
