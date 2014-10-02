package es.telocompro.service.purchase;

import java.math.BigDecimal;
import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import es.telocompro.model.purchase.Purchase;
import es.telocompro.model.purchase.PurchaseRepository;
import es.telocompro.model.user.User;
import es.telocompro.rest.exception.PurchaseDuplicateException;
import es.telocompro.rest.exception.PurchaseNotFoundException;
import es.telocompro.rest.exception.UserNotFoundException;
import es.telocompro.service.user.UserService;

@Service("purchaseService")
public class PurchaseServiceImpl implements PurchaseService {
	
	@Autowired
	PurchaseRepository purchaseRepository;
	@Autowired
	UserService userService;

	@Override
	public Purchase addPurchase(String txn_id, Calendar payment_date, Long userId, String payment_status, 
			String item_name, int item_number, String ipn_track_id, String receiver_id, BigDecimal mc_gross, BigDecimal mc_fee,
			String first_name, String last_name, String payer_email, String residence_country) 
					throws UserNotFoundException, PurchaseDuplicateException {
		
		// Check if user exists
		User user = userService.findUserById(userId);
		if (user == null) throw new UserNotFoundException(userId);
		
		// Create purchase
		Purchase purchase = new Purchase(txn_id, payment_date, user, payment_status, item_name, item_number, 
				ipn_track_id, receiver_id, mc_gross, mc_fee, first_name, last_name, payer_email, residence_country);
		
		// Check if purchase is duplicate
		if (purchaseRepository.findOne(txn_id) != null)
			throw new PurchaseDuplicateException(txn_id);
		
		return purchaseRepository.save(purchase);
		
	}

	@Override
	public Page<Purchase> getAllPurchasesByUserId(Long userId, int page, int size) throws UserNotFoundException {
		if (userService.findUserById(userId) == null)
			throw new UserNotFoundException(userId);
		
		return purchaseRepository.findAllPurchasesByUserId(userId, new PageRequest(page, size));
	}

	@Override
	public Page<Purchase> getAllPurchasesByLogin(String login, int page, int size) throws UserNotFoundException {
		if (userService.findUserByLogin(login) == null)
			throw new UserNotFoundException(login);
		
		return purchaseRepository.findAllPurchasesByUserName(login, new PageRequest(page, size));
	}

	@Override
	public Purchase updatePurchaseState(String txn_id, String payment_status) throws PurchaseNotFoundException {
		Purchase purchase = purchaseRepository.findOne(txn_id);
		
		if (purchase == null) throw new PurchaseNotFoundException(txn_id);
		
		purchase.setPayment_status(payment_status);
		
		return purchaseRepository.save(purchase);
	}

}
