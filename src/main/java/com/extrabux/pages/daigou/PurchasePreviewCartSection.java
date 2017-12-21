package com.extrabux.pages.daigou;

import java.util.List;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class PurchasePreviewCartSection extends BasePage {
	WebElement subtotal;
	WebElement shipping;
	WebElement tax;
	WebElement couponField;
	WebElement couponBtn;
	WebElement couponDiscount;
	WebElement total;
	WebElement cashback;

	List<WebElement> errors;

	public PurchasePreviewCartSection(WebDriver driver) {
		super(driver);
	}

	private String getSubtotal() {
		return subtotal.getText().replace("$", "");
	}

	public String getShipping() {
		return shipping.getText().replace("$", "");
	}

	public String getTax() {
		return tax.getText().replace("$", "");
	}

	private String getCouponDiscount() {
		return couponDiscount.getText().replace("$", "").replace("-", "");
	}

	public String getTrueTotal() {
		float total = (Float.parseFloat(getSubtotal()) + Float.parseFloat(getShipping()) + Float.parseFloat(getTax())) - Float.parseFloat(getCouponDiscount());
		total = Math.round(total * 100);
		total /=100;
		return Float.toString(total);
	}

	public String getTotal() {
		return total.getText().replace("$", "");
	}

	public String getDiscount() {
		return cashback.getText().replace("$", "");
	}

	public boolean verifySubtotal(String sentSubtotal) {
		String subtotal = getSubtotal();
		return subtotal.equals(sentSubtotal);
	}

	public boolean verifyShipping(String sentShipping) {
		String shipping = getShipping();
		return shipping.equals(sentShipping);
	}

	public boolean verifyTax(String sentTax) {
		String tax = getTax();
		return tax.equals(sentTax);
	}

	public boolean verifyTotal(String sentTotal) {
		if(Math.abs(Float.parseFloat(sentTotal) - Float.parseFloat(getTotal())) > 0.00000000001){
			return false;
		}
		return true;
	}

	public boolean verifyTotal() {
		if(Math.abs(Float.parseFloat(getTrueTotal()) - Float.parseFloat(getTotal())) > 0.00000000001){
			return false;
		}
		return true;
	}

	public boolean verifyDiscount(String sentDiscount) {
		String discount = getDiscount();
		return discount.equals(sentDiscount);
	}

	public boolean verifyCouponDiscount(String sentCouponDiscount) {
		String couponDiscount = getCouponDiscount();
		return couponDiscount.equals(sentCouponDiscount);
	}

	public Verify verifyPreviewCartInfo(String subtotal, String discount) {
		Verify verify = new Verify();
		String error = "";
		String actualSubtotal = getSubtotal();
		String actualDiscount = getDiscount();
		String actualTotal = getTotal();
		if (!verifySubtotal(subtotal)) {
			error += "Subtotal did not match; Expected " + subtotal + " but found " + actualSubtotal + ". ";
		}
		if (!verifyDiscount(discount)) {
			error += "Discount did not match; Expected " + discount + " but found " + actualDiscount + ". ";
		}
		if (!verifyTotal()) {
			error += "Total did not match; Expected " + getTrueTotal() + " but found " + actualTotal + ". ";
		}
		verify.setError(error);
		verify.setVerified(verifySubtotal(subtotal) && verifyDiscount(discount) && verifyTotal());
		return verify;
	}

	public Verify verifyPreviewCartInfo(String subtotal, String discount, String coupon) {
		Verify verify = new Verify();
		String error = "";
		String actualSubtotal = getSubtotal();
		String actualDiscount = getDiscount();
		String actualCouponDiscount = getCouponDiscount();
		String actualTotal = getTotal();
		if (!verifySubtotal(subtotal)) {
			error += "Subtotal did not match; Expected " + subtotal + " but found " + actualSubtotal + ". ";
		}
		if (!verifyDiscount(discount)) {
			error += "Discount did not match; Expected " + discount + " but found " + actualDiscount + ". ";
		}
		if (!verifyCouponDiscount(coupon)) {
			error += "Coupon Discount did not match; Expected " + coupon + " but found " + actualCouponDiscount + ". ";
		}
		if (!verifyTotal()) {
			error += "Total did not match; Expected " + getTrueTotal() + " but found " + actualTotal + ". ";
		}
		verify.setError(error);
		verify.setVerified(verifySubtotal(subtotal) && verifyDiscount(discount) && verifyCouponDiscount(coupon) && verifyTotal());
		return verify;
	}
}
