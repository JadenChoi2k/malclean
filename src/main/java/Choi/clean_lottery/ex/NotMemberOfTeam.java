package Choi.clean_lottery.ex;

public class NotMemberOfTeam extends IllegalArgumentException {
    public NotMemberOfTeam() {
        super();
    }

    public NotMemberOfTeam(String s) {
        super(s);
    }

    public NotMemberOfTeam(String message, Throwable cause) {
        super(message, cause);
    }

    public NotMemberOfTeam(Throwable cause) {
        super(cause);
    }
}
