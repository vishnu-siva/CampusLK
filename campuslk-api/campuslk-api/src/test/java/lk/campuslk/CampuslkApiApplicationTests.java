package lk.campuslk;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"app.jwt.secret=test_jwt_secret_that_is_long_enough_for_hmac_sha_signing_1234567890"
})
class CampuslkApiApplicationTests {

	@Test
	void contextLoads() {
	}

}
