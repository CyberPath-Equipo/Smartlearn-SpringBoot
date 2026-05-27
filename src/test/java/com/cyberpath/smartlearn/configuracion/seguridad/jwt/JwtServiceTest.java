package com.cyberpath.smartlearn.configuracion.seguridad.jwt;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

@SpringJUnitConfig(classes = JwtService.class)
@TestPropertySource(properties = {
		"jwt.secret=miClaveSuperSecretaParaPruebasJwt2026SmartLearnMuyLarga123456789",
		"jwt.expiration=1000",
		"jwt.refresh-expiration=2000"
})
class JwtServiceTest {

	@Autowired
	private JwtService jwtService;

	@Test
	void shouldGenerateDifferentTokenTypes() {
		String subject = "usuario@smartlearn.com";

		String accessToken = jwtService.generarToken(subject);
		String refreshToken = jwtService.generarRefreshToken(subject);

		Assertions.assertThat(jwtService.isAccessTokenValid(accessToken)).isTrue();
		Assertions.assertThat(jwtService.isRefreshTokenValid(refreshToken)).isTrue();
		Assertions.assertThat(jwtService.isRefreshTokenValid(accessToken)).isFalse();
		Assertions.assertThat(jwtService.isAccessTokenValid(refreshToken)).isFalse();
		Assertions.assertThat(jwtService.obtenerSubject(accessToken)).isEqualTo(subject);
		Assertions.assertThat(jwtService.obtenerSubject(refreshToken)).isEqualTo(subject);
		Assertions.assertThat(jwtService.obtenerTipoToken(accessToken)).isEqualTo("ACCESS");
		Assertions.assertThat(jwtService.obtenerTipoToken(refreshToken)).isEqualTo("REFRESH");
	}
}

