package notyet.newword.model;

public class Question {
    String nickname;
    String uid;
    String title;
    String contents;
    String time;

    public Question(String nickname, String uid, String title, String contents,String time) {
        this.nickname = nickname;
        this.uid = uid;
        this.title = title;
        this.contents = contents;
        this.time=time;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}
