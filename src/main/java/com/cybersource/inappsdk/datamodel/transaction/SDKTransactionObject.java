package com.cybersource.inappsdk.datamodel.transaction;

import com.cybersource.inappsdk.datamodel.transaction.fields.SDKBillTo;
import com.cybersource.inappsdk.datamodel.transaction.fields.SDKCardData;

/**
 * This class represents a transaction that is sent to a reader and then received back from it.
 * 
 * @author fzubair
 */
public abstract class SDKTransactionObject {

	protected String merchantReferenceCode;
	protected SDKTransactionType transactionType;
	protected String transactionTime;
	protected String transactionDate;
	protected SDKCardData cardData;
	protected SDKBillTo billTo;

    SDKTransactionObject() {
    }

	public String getMerchantReferenceCode() {
		return merchantReferenceCode;
	}

	public final String getTransactionTime() {
		return transactionTime;
	}

	public final String getTransactionDate() {
		return transactionDate;
	}

	public SDKCardData getCardData() {
		return cardData;
	}

	public SDKBillTo getBillTo() {
		return billTo;
	}

	public SDKTransactionType getTransactionType() {
		return transactionType;
	}

	/**
	 * A factory method for creating proper transaction object.
	 * 
	 * @return one of transaction objects
	 */
	public static SDKTransactionObject.Builder createTransactionObject() {
		return new SDKEncryptTransactionObject.Builder();
	}

    public static abstract class Builder {
		protected String merchantReferenceCode;
		protected SDKTransactionType transactionType;
		protected String transactionTime;
		protected String transactionDate;
		protected SDKCardData cardData;
        protected SDKBillTo billTo;

        public SDKTransactionObject.Builder merchantReferenceCode(String merchantReferenceCode) {
            this.merchantReferenceCode = merchantReferenceCode;
            return this;
        }

        public SDKTransactionObject.Builder cardData(SDKCardData cardData) {
            this.cardData = cardData;
            return this;
        }

        public SDKTransactionObject.Builder billTo(SDKBillTo billTo) {
            this.billTo = billTo;
            return this;
        }

        public SDKTransactionObject.Builder transactionDate(String transactionDate) {
            this.transactionDate = transactionDate;
            return this;
        }

        public SDKTransactionObject.Builder transactionTime(String transactionTime) {
            this.transactionTime = transactionTime;
            return this;
        }

        public abstract SDKEncryptTransactionObject build();

	}
}
