package com.evisitor.ui.dialog.country;

import com.evisitor.data.model.CountryResponse;

public interface CountrySelectionCallback {
    void onSelect(CountryResponse countryResponse);
}
