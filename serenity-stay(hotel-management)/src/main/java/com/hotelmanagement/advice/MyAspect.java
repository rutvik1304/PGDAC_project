package com.hotelmanagement.advice;

import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class MyAspect {
	
	// * represent any RETURN TYPE
	//*  com.code.PaymentServiceImp.makePayment()  will execute before make Payment whose return type can be anything
	//*  com.code.PaymentServiceImp.*  will execute before every method present in PaymentServiceImp
	//*  com.code.PaymentServiceImp.makePayment(..)  this dot dot .. indicates method has arguments 
	
//	@Before(value = "execution(* com.code.PaymentServiceImp.makePayment(..))")
//	public void printBefore() {
//		System.out.println("Payment Started");
//	}
//
//	@After(value = "execution(* com.code.PaymentServiceImp.makePayment(..))")
//	public void printAfter() {
//		System.out.println("Payment Successful");
//	}
//	
}
