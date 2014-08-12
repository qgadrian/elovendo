package es.telocompro.model.purchase;

import java.math.BigDecimal;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import es.telocompro.model.user.User;

@Entity
@Table(name = "purchase")
public class Purchase {
	
	@Id
    @Column(name = "txn_id")
    private String txn_id;
	
    @Column(columnDefinition="DATETIME", name = "payment_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar payment_date;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "userId")
    private User user;

    @Column(name = "payment_status")
    private String payment_status;
    @Column(name = "item_name")
    private String item_name;
    @Column(name = "ipn_track_id")
    private String ipn_track_id;
    @Column(name = "receiver_id")
    private String receiver_id;
    
    @Column(name = "item_number")
    private int item_number;
    
    @Column(name = "mc_gross")
    private BigDecimal mc_gross;
    @Column(name = "mc_fee")
    private BigDecimal mc_fee;
    
    @Column(name = "first_name")
    private String first_name;
    @Column(name = "last_name")
    private String last_name;
    @Column(name = "payer_email")
    private String payer_email;
    @Column(name = "residence_country")
    private String residence_country;
    
    protected Purchase () { }
    
	public Purchase(String txn_id, Calendar payment_date, User user,
			String payment_status, String item_name, int item_number, String ipn_track_id,
			String receiver_id, BigDecimal mc_gross, BigDecimal mc_fee,
			String first_name, String last_name, String payer_email,
			String residence_country) {
		this.txn_id = txn_id;
		this.payment_date = payment_date;
		this.user = user;
		this.payment_status = payment_status;
		this.item_name = item_name;
		this.ipn_track_id = ipn_track_id;
		this.receiver_id = receiver_id;
		this.mc_gross = mc_gross;
		this.mc_fee = mc_fee;
		this.first_name = first_name;
		this.last_name = last_name;
		this.payer_email = payer_email;
		this.residence_country = residence_country;
	}

	public String getTxn_id() {
		return txn_id;
	}

	public Calendar getPayment_date() {
		return payment_date;
	}

	public User getUser() {
		return user;
	}

	public String getPayment_status() {
		return payment_status;
	}

	public String getItem_name() {
		return item_name;
	}
	
	public int getItem_number() {
		return item_number;
	}

	public String getIpn_track_id() {
		return ipn_track_id;
	}

	public String getReceiver_id() {
		return receiver_id;
	}

	public BigDecimal getMc_gross() {
		return mc_gross;
	}

	public BigDecimal getMc_fee() {
		return mc_fee;
	}

	public String getFirst_name() {
		return first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public String getPayer_email() {
		return payer_email;
	}

	public String getResidence_country() {
		return residence_country;
	}

	public void setPayment_status(String payment_status) {
		this.payment_status = payment_status;
	}
}
