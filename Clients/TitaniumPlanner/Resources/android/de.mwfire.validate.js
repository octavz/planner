function validate(inputFields, options) {
    if (!_) return Ti.API.error("[validationModule] this plugin requires underscore!");
    if (!inputFields || !_.isArray(inputFields) || !inputFields.length) return Ti.API.error("[validationModule] Please specify at least one input field!");
    options = options || {};
    errors = [];
    for (var i = 0, max = inputFields.length; max > i; i++) {
        var inputField = inputFields[i];
        inputField.rules && validateField(inputField);
    }
    if (errors.length > 0) {
        options.showAlert && showAlert();
        return false;
    }
    return true;
}

function validateField(inputField) {
    var rules = inputField.rules.split("|"), indexOfRequired = inputField.rules.indexOf("required"), isEmpty = !inputField.value || "" === inputField.value || void 0 === inputField.value;
    for (var i = 0, ruleLength = rules.length; ruleLength > i; i++) {
        var method = rules[i], param = null, failed = false, parts = ruleRegex.exec(method);
        if (!inputField.isSwitch && -1 === indexOfRequired && isEmpty) continue;
        if (parts) {
            method = parts[1];
            param = parts[2];
        }
        "!" === method.charAt(0) && (method = method.substring(1, method.length));
        "function" == typeof hooks[method] && ("matches" === method ? hooks[method].apply(this, [ inputField.value, inputField.matchField.value ]) || (failed = true) : hooks[method].apply(this, [ inputField.value, param ]) || (failed = true));
        if (failed) {
            var source = messages[method];
            var message = source.replace("%s", inputField.name);
            "matches" === method && (param = inputField.matchField.name);
            param && (message = message.replace("%p", param));
            errors.push({
                message: message
            });
            break;
        }
    }
    return;
}

function showAlert() {
    var alertText = "";
    for (var i = 0, max = errors.length; max > i; i++) alertText += errors[i].message + "\n";
    Ti.UI.createAlertDialog({
        title: L("validationErrorTitle", "Validation errors!"),
        message: alertText
    }).show();
    return;
}

var errors = [];

var _ = require("alloy")._;

var messages = {
    required: L("validationRequiredField", "The %s field is required."),
    matches: L("validationMatchField", "The %s field does not match the %p field."),
    valid_email: L("validationValidEmail", "The %s field must contain a valid email address."),
    valid_emails: L("validationValidEmails", "The %s field must contain all valid email addresses."),
    min_length: L("validationMinLength", "The %s field must be at least %p characters in length."),
    max_length: L("validationMaxLength", "The %s field must not exceed %p characters in length."),
    exact_length: L("validationExactLength", "The %s field must be exactly %p characters in length."),
    greater_than: L("validationGreaterThan", "The %s field must contain a number greater than %p."),
    is_checked: L("validationIsChecked", "%s has to be set."),
    agree_on: L("validationAgreeOn", "You have to agree to %s."),
    age_at_least: L("validationAgeAtLeast", "You must be at least %p years old to continue!"),
    less_than: L("validationLessThan", "The %s field must contain a number less than %p."),
    alpha: L("validationAlpha", "The %s field must only contain alphabetical characters."),
    alpha_numeric: L("validationAlphaNumeric", "The %s field must only contain alpha-numeric characters."),
    alpha_dash: L("validationDash", "The %s field must only contain alpha-numeric characters, underscores, and dashes."),
    numeric: L("validationNumeric", "The %s field must contain only numbers."),
    integer: L("validationInteger", "The %s field must contain an integer."),
    decimal: L("validationDecimal", "The %s field must contain a decimal number."),
    is_natural: L("validationIsNatural", "The %s field must contain only positive numbers."),
    is_natural_no_zero: L("validationIsNaturalNoZero", "The %s field must contain a number greater than zero."),
    valid_ip: L("validationValidIp", "The %s field must contain a valid IP."),
    valid_base64: L("validationValidBase64", "The %s field must contain a base64 string."),
    valid_credit_card: L("validationValidCreditCard", "The %s field must contain a valid credit card number."),
    valid_url: L("validationValidUrl", "The %s field must contain a valid URL."),
    valid_zip_code: L("validationValidZipCode", "The %s field must be a valid Zip Code™.")
};

var ruleRegex = /^(.+?)\[(.+)\]$/, numericRegex = /^[0-9]+$/, integerRegex = /^\-?[0-9]+$/, decimalRegex = /^\-?[0-9]*\.?[0-9]+$/, emailRegex = /^[a-zA-Z0-9.!#$%&amp;'*+\-\/=?\^_`{|}~\-]+@[a-zA-Z0-9\-]+(?:\.[a-zA-Z0-9\-]+)*$/, alphaRegex = /^[a-z]+$/i, alphaNumericRegex = /^[a-z0-9]+$/i, alphaDashRegex = /^[a-z0-9_\-]+$/i, naturalRegex = /^[0-9]+$/i, naturalNoZeroRegex = /^[1-9][0-9]*$/i, ipRegex = /^((25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})\.){3}(25[0-5]|2[0-4][0-9]|1[0-9]{2}|[0-9]{1,2})$/i, base64Regex = /[^a-zA-Z0-9\/\+=]/i, numericDashRegex = /^[\d\-\s]+$/, urlRegex = /^((http|https):\/\/(\w+:{0,1}\w*@)?(\S+)|)(:[0-9]+)?(\/|\/([\w#!:.?+=&%@!\-\/]))?$/, zipCodeRegex = /^[0-9]{5}(-[0-9]{4})?$/;

var hooks = {
    required: function(value) {
        return null !== value && "" !== value;
    },
    matches: function(value, param) {
        return value === param;
    },
    valid_email: function(value) {
        return emailRegex.test(value);
    },
    min_length: function(value, length) {
        if (!numericRegex.test(length)) return false;
        return value.length >= parseInt(length, 10);
    },
    max_length: function(value, length) {
        if (!numericRegex.test(length)) return false;
        return value.length <= parseInt(length, 10);
    },
    exact_length: function(value, length) {
        if (!numericRegex.test(length)) return false;
        return value.length === parseInt(length, 10);
    },
    greater_than: function(value, param) {
        if (!decimalRegex.test(value)) return false;
        return parseFloat(value) > parseFloat(param);
    },
    less_than: function(value, param) {
        if (!decimalRegex.test(value)) return false;
        return parseFloat(value) < parseFloat(param);
    },
    age_at_least: function(value, param) {
        var today = new Date();
        var birthDate = new Date(value);
        var age = today.getFullYear() - birthDate.getFullYear();
        var m = today.getMonth() - birthDate.getMonth();
        (0 > m || 0 === m && today.getDate() < birthDate.getDate()) && age--;
        return parseInt(age) >= parseInt(param);
    },
    is_checked: function(value) {
        return value;
    },
    agree_on: function(value) {
        return value;
    },
    alpha: function(value) {
        return alphaRegex.test(value);
    },
    alpha_numeric: function(value) {
        return alphaNumericRegex.test(value);
    },
    alpha_dash: function(value) {
        return alphaDashRegex.test(value);
    },
    numeric: function(value) {
        return numericRegex.test(value);
    },
    integer: function(value) {
        return integerRegex.test(value);
    },
    decimal: function(value) {
        return decimalRegex.test(value);
    },
    is_natural: function(value) {
        return naturalRegex.test(value);
    },
    is_natural_no_zero: function(value) {
        return naturalNoZeroRegex.test(value);
    },
    valid_ip: function(value) {
        return ipRegex.test(value);
    },
    valid_base64: function(value) {
        return base64Regex.test(value);
    },
    valid_url: function(value) {
        return urlRegex.test(value);
    },
    valid_credit_card: function(value) {
        if (!numericDashRegex.test(value)) return false;
        var nCheck = 0, nDigit = 0, bEven = false;
        var strippedField = value.replace(/\D/g, "");
        for (var n = strippedField.length - 1; n >= 0; n--) {
            var cDigit = strippedField.charAt(n);
            nDigit = parseInt(cDigit, 10);
            bEven && (nDigit *= 2) > 9 && (nDigit -= 9);
            nCheck += nDigit;
            bEven = !bEven;
        }
        return nCheck % 10 === 0;
    },
    valid_zip_code: function(value) {
        return zipCodeRegex.test(value);
    }
};

exports.validate = validate;