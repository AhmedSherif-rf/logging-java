package com.webbats.payment.broker.common.annotation.brokeruser;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation to be used on methods or types (classes) that require an authenticated
 * user to have administrative privileges.
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface BrokerUser {
}
