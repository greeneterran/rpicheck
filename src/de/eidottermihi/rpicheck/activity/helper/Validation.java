package de.eidottermihi.rpicheck.activity.helper;

import org.apache.commons.lang3.StringUtils;

import android.content.Context;
import android.widget.Button;
import android.widget.EditText;
import de.eidottermihi.rpicheck.R;

/**
 * A helper class for validating user input.
 * 
 * @author Michael
 * 
 */
public class Validation {

	/**
	 * Validates if user input is valid.
	 * 
	 * @param authMethod
	 *            0 = ssh password, 1 = private key, 2 = private key with
	 *            passphrase
	 * @param name
	 * @param host
	 * @param user
	 * @param password
	 * @param port
	 * @param sudoPass
	 * @return true, if input is valid
	 */
	public boolean validatePiEditData(Context context, int authMethod,
			EditText name, EditText host, EditText user, EditText password,
			EditText port, EditText sudoPass, EditText keyPassphrase,
			Button keyChooser, boolean alwaysAskChecked, String keyfilePath) {
		boolean dataValid = true;
		// check non-optional fields
		if (!checkNonOptionalTextField(name,
				context.getString(R.string.validation_msg_name))) {
			dataValid = false;
		}
		if (!checkNonOptionalTextField(host,
				context.getString(R.string.validation_msg_host))) {
			dataValid = false;
		}
		if (!checkNonOptionalTextField(user,
				context.getString(R.string.validation_msg_user))) {
			dataValid = false;
		}
		if (!validatePort(port)) {
			dataValid = false;
		}
		// check auth method
		if (authMethod == 0) {
			// ssh password must be present
			if (!checkNonOptionalTextField(password,
					context.getString(R.string.validation_msg_password))) {
				dataValid = false;
			}
		} else if (authMethod == 1) {
			// a keyfile must be present
			if (keyfilePath == null || StringUtils.isBlank(keyfilePath)) {
				keyChooser.setError(context
						.getString(R.string.validation_msg_keyfile));
				dataValid = false;
			}
		} else if (authMethod == 2) {
			// keyfile must be present
			if (keyfilePath == null || StringUtils.isBlank(keyfilePath)) {
				keyChooser.setError(context
						.getString(R.string.validation_msg_keyfile));
				dataValid = false;
			}
			// if always asked is unchecked, passphrase must be present
			if (!alwaysAskChecked) {
				if (!checkNonOptionalTextField(
						keyPassphrase,
						context.getString(R.string.validation_msg_key_passphrase))) {
					dataValid = false;
				}
			}
		}
		return dataValid;
	}

	/**
	 * Validates if user input for core data is valid.
	 * 
	 * @param name
	 * @param host
	 * @param user
	 * @return true, if input is valid
	 */
	public boolean validatePiCoreData(Context context, EditText name,
			EditText host, EditText user) {
		boolean dataValid = true;
		if (!checkNonOptionalTextField(name,
				context.getString(R.string.validation_msg_name))) {
			dataValid = false;
		}
		if (!checkNonOptionalTextField(host,
				context.getString(R.string.validation_msg_host))) {
			dataValid = false;
		}
		if (!checkNonOptionalTextField(user,
				context.getString(R.string.validation_msg_user))) {
			dataValid = false;
		}
		return dataValid;
	}

	/**
	 * Checks if a Textfield is not blank.
	 * 
	 * @param textfield
	 *            the EditText to check
	 * @param errorMessage
	 *            the errorMessage to set if validation fails
	 * @return true, if valid, else false
	 */
	public boolean checkNonOptionalTextField(EditText textfield,
			String errorMessage) {
		// get text
		final String text = textfield.getText().toString().trim();
		if (text == null || StringUtils.isBlank(text)) {
			textfield.setError(errorMessage);
			return false;
		}
		return true;
	}

	public boolean validatePort(EditText editTextSshPort) {
		boolean portValid = true;
		// range 1 to 65535
		try {
			final Long sshPort = Long.parseLong(editTextSshPort.getText()
					.toString());
			if (sshPort < 1 || sshPort > 65535) {
				portValid = false;
			}
		} catch (NumberFormatException e) {
			portValid = false;
		}
		if (!portValid) {
			editTextSshPort.setError(editTextSshPort.getContext().getText(
					R.string.validation_msg_port));
		}
		return portValid;
	}

}
