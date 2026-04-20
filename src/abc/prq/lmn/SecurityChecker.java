package abc.prq.lmn;

import com.varnit.jain.webRock.annotations.*;
import com.varnit.jain.webRock.scope.*;

@InjectSessionScope
public class SecurityChecker {
    private SessionScope sessionScope;

    public void setSessionScope(SessionScope sessionScope) {
        this.sessionScope = sessionScope;
    }

    public void check() {
        Object token = sessionScope.getAttribute("token");
        if (token == null) {
            System.out.println("SecurityChecker: Token not found in session!");
            throw new RuntimeException("Unauthorized Access");
        }
        System.out.println("SecurityChecker: Access Granted!");
    }
}
