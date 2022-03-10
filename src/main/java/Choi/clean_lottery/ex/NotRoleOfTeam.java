package Choi.clean_lottery.ex;

public class NotRoleOfTeam extends IllegalArgumentException {
    public NotRoleOfTeam() {
        super();
    }

    public NotRoleOfTeam(String s) {
        super(s);
    }

    public NotRoleOfTeam(String message, Throwable cause) {
        super(message, cause);
    }

    public NotRoleOfTeam(Throwable cause) {
        super(cause);
    }
}
