import org.jose4j.lang.JoseException;
import org.junit.Test;

class JWTUtilTest {

    @Test
    public void createJWT() throws JoseException {
        JWTUtil.createJWT(1234,30);
    }
}