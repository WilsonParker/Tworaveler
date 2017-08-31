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
                ,KEY_SCHEDULE_MODEL= "ScheduleModel"
                ,KEY_SCHEDULE_DAY_MODEL= "ScheduleDayModel"
                ,KEY_NICKNAME= "Nickname"

                ,KEY_STARTED_BY= "StartedBy"
                ,KEY_POSITION= "Position"

                ,KEY_TRIPDATE= "TripDate"
                ,KEY_STARTDATE= "StartDate"
                ,KEY_ENDDATE= "EndDate"
                ,KEY_DATE = "Date"
                ;
        public static final int
                RESULT_CODE_SUCCESS = 0x0000
                ,RESULT_CODE_FAIL = 0x1111
                ,RESULT_CODE_SEARCH_CITY = 0x0001
                ,RESULT_CODE_REGIST_DETAIL= 0x0010
                , RESULT_CODE_REGIST_DAY_LIST = 0x0011

                ,RESULT_CODE_CITY_MODEL= 0x0100
                , RESULT_CODE_SCHEDULE_MODEL = 0x0101

                ,KEY_MYPAGE= 0x0001
                ,KEY_FEED= 0x0010;

        ;

    }

    public class Bundle{
        public static final String
                KEY_STARTDATE = "StartDate"
                ,KEY_ENDDATE = "EndDate"
                ,KEY_DATE = "Date"
                ,KEY_FILTER_TYPE= "filter_type"
                ,KEY_SERIALIZABLE= "serializable"
                ,KEY_NICKNAME= "Nickname"
                ;
    }

    public class Bag{
        public static final String
                CATEGORY_TICKET = "ticket"    // 교통티켓
                ,CATEGORY_MAP= "map"    // 지도
                ,CATEGORY_SUBWAY = "subway"    // 노선도
                ,CATEGORY_SHOP = "shop"    // 쇼핑
                ,CATEGORY_SALE= "sale"    // 할인권
                ;
    }

    public class Key{
        public static final String
                // User
                KEY_USER_NO = "user_no"

                // Bag
                , KEY_CATEGORY_THEME= "category_theme"

                // File
                , KEY_USER_FILE= "userfile"
                ;
    }

    public class Size{
        public static final int
                SIZE_FEED_LIST_COUNT= 5
                , SIZE_MY_PROFILE_LIST_COUNT= 5
                ;
    }

    public class RegularExpression{
        public static final String REG_EMAIL = "(^[a-zA-z0-9]+@[a-z]+.(com|org|net)+$)"
                ,REG_PASSWORD="^([a-zA-Z0-9!@#$%]){8,}$"
                ,REG_NICKNAME="^([a-zA-Z0-9가-힣]){4,16}$"
                ,REG_DATE="^([0-9]){4}-([0-9]){1,2}-([0-9]){1,2}$"
                ,REG_TIME ="^([0-9]){1,2}:([0-9]){1,2}$"
                ,REG_STRING_DATE_FOR_INTGER="(-)"
//                ,REG_STRING_DATE_FOR_INTGER="(년|월|일)"

                ,FORMAT_DATE="yyyy-MM-dd"
                ,FORMAT_DATE_TIME="HH:mm"
                ,FORMAT_DATE_REGIST_DETAIL="yyyy.MM"
                ;
    }

    public class Network{
        public static final int
                CODE_SUCCESS = 200                          // 성공
                , CODE_NOT_LOGIN = 100                      // 비로그인
                , CODE_EMAIL_CONFLICT = 201                 // 이메일 중복
                , CODE_NICKNAME_CONFLICT = 202              // 닉네임 중복
                , CODE_SIGNOUT_USER= 203                    // 탈퇴된 아이디
                , CODE_EMAIL_PW_INCORRECT = 204             // 이메일과 비밀번호 불일치
                , CODE_PW_INCORRECT = 205                   // 비밀번호 불일치
                , CODE_EMAIL_INCORRECT= 206                 // 이메일 불일치
                , CODE_NONE_SESSION= 207                    // 유저 데이터 없음
                , CODE_BAG_ITEM_FIND_FAIL= 209              // 여행가방 아이템 찾기 실패
                , CODE_USER_OR_BAG_ITEM_INCOREECT= 210      // 잘못된 유저 번호 또는 아이템 번호


                , CODE_ERROR = 0                            // 에러 발생
                , CODE_FAIL = 500                           // 실패
                ;
    }
}
