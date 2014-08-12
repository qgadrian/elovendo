package es.telocompro.service.purchase;

import java.math.BigDecimal;
import java.util.Calendar;

import org.springframework.data.domain.Page;

import es.telocompro.model.purchase.Purchase;
import es.telocompro.rest.controller.exception.PurchaseDuplicateException;
import es.telocompro.rest.controller.exception.PurchaseNotFoundException;
import es.telocompro.rest.controller.exception.UserNotFoundException;

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
