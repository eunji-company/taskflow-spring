package indiv.abko.taskflow.integration;

import static org.assertj.core.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import indiv.abko.taskflow.domain.user.entity.UserRole;
import indiv.abko.taskflow.global.jwt.JwtUtil;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
public class TeamApiIntegrationTest {

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private JwtUtil jwtUtil;

	@Nested
	class CreateTeam {

		@Test
		void ADMIN_역할은_팀_생성에_성공한다() {
			// given
			String adminToken = jwtUtil.createAccessToken(1L, UserRole.ADMIN);
			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(adminToken);
			headers.setContentType(MediaType.APPLICATION_JSON);

			Map<String, String> requestBody = Map.of(
				"name", "Test team name",
				"description", "Test team description"
			);
			HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

			// when
			ResponseEntity<Void> response = restTemplate.postForEntity("/api/teams", requestEntity, Void.class);

			// then
			assertThat(response.getStatusCode()).isIn(HttpStatus.CREATED, HttpStatus.OK);
		}

		@Test
		void USER_역할은_팀_생성_권한이_없어_실패한다() {
			// given
			String userToken = jwtUtil.createAccessToken(1L, UserRole.USER);
			HttpHeaders headers = new HttpHeaders();
			headers.setBearerAuth(userToken);
			headers.setContentType(MediaType.APPLICATION_JSON);

			Map<String, String> requestBody = Map.of(
				"name", "Test team name",
				"description", "Test team description"
			);
			HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

			// when
			ResponseEntity<Void> response = restTemplate.postForEntity("/api/teams", requestEntity, Void.class);

			// then
			assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
		}
	}
}
