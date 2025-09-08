package indiv.abko.taskflow.global.auth;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.springframework.security.test.context.support.WithSecurityContext;

import indiv.abko.taskflow.domain.user.entity.UserRole;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockAuthMemberSecurityContextFactory.class)
public @interface WithMockAuthMember {
	long memberId() default 1L;

	UserRole userRole() default UserRole.USER;
}
