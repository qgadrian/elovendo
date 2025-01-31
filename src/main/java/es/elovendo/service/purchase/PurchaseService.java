package es.elovendo.service.purchase;

import java.math.BigDecimal;
import java.util.Calendar;

import org.springframework.data.domain.Page;

import es.elovendo.model.purchase.Purchase;
import es.elovendo.rest.exception.PurchaseDuplicateException;
import es.elovendo.rest.exception.PurchaseNotFoundException;
import es.elovendo.rest.exception.UserNotFoundException;

public interface PurchaseService {
	
	/*
	 * CREATE
	 */
	public Purchase addPurchase(String txn_id, Calendar payment_date, Long userId, String payment_status, 
			String item_name, int item_number, String ipn_track_id, String receiver_id, BigDecimal mc_gross, BigDecimal mc_fee,
			String first_name, String last_name, String payer_email, String residence_country) 
					throws UserNotFoundException, PurchaseDuplicateException;
	
	/*
	 * READ
	 */
	
	/**
	 * Finds all purchases by a given user
	 * @param userId
	 * @return Purchases
	 */
	public Page<Purchase> getAllPurchasesByUserId(Long userId, int page, int size) throws UserNotFoundException;
	
	/**
	 * Finds all purchases by a given user
	 * @param userId
	 * @return Purchases
	 */
	public Page<Purchase> getAllPurchasesByLogin(String login, int page, int size) throws UserNotFoundException;
	
	/*
	 * UPDATE
	 */
	
	/**
	 * Updates the payment's status
	 * @param payment_status
	 * @return Payment update
	 */
	public Purchase updatePurchaseState(String txn_id, String payment_status) throws PurchaseNotFoundException;

}
