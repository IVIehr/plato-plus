package menu;

public class User {
    private String username;
    private String password;
    private String email;
    private String question;
    private String answer;
    private String gender;

    public User(String username,String password){
        setUsername(username);
        setPassword(password);
    }
    public User(String username, String password, String email, String question, String answer,String gender){
        setUsername(username);
        setPassword(password);
        setEmail(email);
        setQuestion(question);
        setAnswer(answer);
        setGender(gender);
    }

    public void setUsername(String userName) {
        this.username = userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getGender() {
        return gender;
    }
}
