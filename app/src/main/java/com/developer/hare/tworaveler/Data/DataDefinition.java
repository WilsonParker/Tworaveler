package com.developer.hare.tworaveler.Data;

/**
 * Created by Hare on 2017-08-01.
 */

public class DataDefinition {

    public class Intent{
        public static final String
                 KEY_ITEMS = "intent_items"
                ,KEY_TITLE = "intent_title"
                ,KEY_CITYMODEL= "CityModel"
                ,KEY_BAGMODEL= "BagModel"
                ,KEY_BAGMODELS= "BagModels"
                ,KEY_POSITION= "Position"
                ,KEY_STARTDATE= "StartDate"
                ,KEY_ENDDATE= "EndDate"
                ;
        public static final int
                RESULT_CODE_SUCCESS = 0x0000
                ,RESULT_CODE_FAIL = 0x1111
                ,RESULT_CODE_SEARCH_CITY = 0x0001
                ;

    }

    public class Bundle{
        public static final String
                KEY_STARTDATE = "StartDate"
                ,KEY_ENDDATE = "EndDate"
                ,KEY_DATE = "Date"
                ;
    }

    public class Bag{
       /* public static final String
                CATEGORY_TRAFFIC= "traffic"    // 교통티켓
                ,CATEGORY_SAIL= "sail"    // 할인권
                ,CATEGORY_ROUTE= "route"    // 노선도
                ,CATEGORY_MAP= "map"    // 지도
                ,CATEGORY_SHOPPING= "shopping"    // 쇼핑
                ;*/
    }

    public class RegularExpression{
        public static final String REG_EMAIL = "(^[a-zA-z0-9]+@[a-z]+.(com|org|net)+$)"
                ,REG_PASSWORD="^([a-zA-Z0-9!@#$%]){8,}$"
                ,REG_NICKNAME="^([a-zA-Z0-9]){4,16}$"
                ,REG_STRING_DATE_FOR_INTGER="(-)"
//                ,REG_STRING_DATE_FOR_INTGER="(년|월|일)"

                ,FORMAT_DATE="yyyy-MM-dd"
                ,FORMAT_DATETIME="HH:mm:ss"
                ;
    }

    public class Font{
        /*public static final String FONT_NotoSansCJKkr_Black = "NotoSansCJKkr-Black.otf"
                ,FONT_NotoSansCJKkr_Bold ="NotoSansCJKkr-Bold.otf"
                ,FONT_NotoSansCJKkr_DemiLight ="NotoSansCJKkr-DemiLight.otf"
                ,FONT_NotoSansCJKkr_Light ="NotoSansCJKkr-Light.otf"
                ,FONT_NotoSansCJKkr_Medium ="NotoSansCJKkr-Medium.otf"
                ,FONT_NotoSansCJKkr_Regular ="NotoSansCJKkr-Regular.otf"
                ,FONT_NotoSansCJKkr_Thin ="NotoSansCJKkr-Thin.otf"
                ,FONT_ ="NotoSansMonoCJKkr-Bold.otf"
                ,FONT_ ="NotoSansMonoCJKkr-Regular.otf"
                ,FONT_ ="Roboto-Black.ttf"
                ,FONT_ ="Roboto-BlackItalic.ttf"
                ,FONT_ ="Roboto-Bold.ttf"
                ,FONT_ ="Roboto-BoldItalic.ttf"
                ,FONT_ ="Roboto-Italic.ttf"
                ,FONT_ ="Roboto-Light.ttf"
                ,FONT_ ="Roboto-LightItalic.ttf"
                ,FONT_ ="Roboto-Medium.ttf"
                ,FONT_ ="Roboto-MediumItalic.ttf"
                ,FONT_ ="Roboto-Regular.ttf"
                ,FONT_ ="Roboto-Thin.ttf"
                ,FONT_ ="Roboto-ThinItalic.ttf"
                ,FONT_ ="RobotoCondensed-Bold.ttf"
                ,FONT_ ="RobotoCondensed-BoldItalic.ttf"
                ,FONT_ ="RobotoCondensed-Italic.ttf"
                ,FONT_ ="RobotoCondensed-Light.ttf"
                ,FONT_ ="RobotoCondensed-LightItalic.ttf"
                ,FONT_ ="FONT_RobotoCondensed-Regular.ttf"
        ;
        */
    }
}
