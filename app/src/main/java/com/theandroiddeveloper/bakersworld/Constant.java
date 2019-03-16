package com.theandroiddeveloper.bakersworld;

public class Constant {
    public static final String SERVER_BASE_URL =
            "https://d17h27t6h515a5.cloudfront.net/topher/2017/May/59121517_baking/";

    public interface IntentExtra {
        String RECIPE_ID = "RECIPE_ID";
        String RECIPE_STEP_INDEX = "RECIPE_STEP_INDEX";
        String IS_MASTER_DETAIL_FLOW = "IS_MASTER_DETAIL_FLOW";
    }

    public interface AppState {
        String ACTIVE_STEP_INDEX = "ACTIVE_STEP_INDEX";
    }
}
