package com.model;

public enum TaskType {

	TaskOrMethod_SettingsGet, // settings/get
	TaskOrMethod_UserGetUser, // user/getUser
	TaskOrMethod_UserLogin, // user/login
	TaskOrMethod_UserLogout, // user/logout
	TaskOrMethod_UserRegister, // user/register
	TaskOrMethod_UserGetPhoto, // user/getPhoto
	TaskOrMethod_UserSetProperties, // user/setProperties
	TaskOrMethod_UserUploadPhoto, // user/uploadPhoto
	TaskOrMethod_UserDeletePhoto, // user/deletePhoto
	TaskOrMethod_UserCheckName, // user/checkName
	TaskOrMethod_UserGetCode, // user/getCode

	TaskOrMethod_UserCheckCode, // user/checkCode
	TaskOrMethod_UserResetPwd, // user/resetPwd
	TaskOrMethod_UserShare, // user/share
	TaskOrMethod_UserUntreated, // user/untreated

	TaskOrMethod_UserListIntegrals, // user/listIntegrals

	TaskOrMethod_UserUpdatePwd, // user/updatepwd
	TaskOrMethod_UserBangdingPhone, // user/bangdingPhone

	TaskOrMethod_MycampusSearch, // mycampus/search
	TaskOrMethod_MycampusMoreServers, // mycampus/moreServers
	TaskOrMethod_MycampusMoreInformations, // mycampus/moreInformations
	TaskOrMethod_MycampusMoreActivities, // mycampus/moreActivities

	TaskOrMethod_MycampusGet, // mycampus/get
	TaskOrMethod_CampusListTypes, // campus/listTypes
	TaskOrMethod_CampusListServers, // campus/listServers
	TaskOrMethod_CampusFavorites, // campus/favorites

	TaskOrMethod_CommunityListCommunities, // community/listCommunities
	TaskOrMethod_CommunityGetCommunity, // community/getCommunity

	TaskOrMethod_ActivityListActivities, // activity/listActivities
	TaskOrMethod_ActivityGetActivity, // activity/getActivity

	TaskOrMethod_HobbygroupListTypes, // hobbygroup/listTypes
	TaskOrMethod_HobbygroupListHobbyGroups, // hobbygroup/listHobbyGroups
	TaskOrMethod_HobbygroupGetHobbyGroup, // hobbygroup/getHobbyGroup
	TaskOrMethod_HobbygroupListPosts, // hobbygroup/listPosts
	TaskOrMethod_HobbygroupDeletePosts, // hobbygroup/deletePosts
	TaskOrMethod_HobbygroupRepliedPosts, // hobbygroup/repliedPosts
	TaskOrMethod_HobbygroupSendPost, // hobbygroup/sendPost
	TaskOrMethod_hobbygroupGetPost, // hobbygroup/getPost

	TaskOrMethod_InformationListInformations, // information/listInformations
	TaskOrMethod_InformationGetInformation, // information/getInformation
	TaskOrMethod_DiscussListDiscussions, // discuss/listDiscussions
	TaskOrMethod_DiscussSendDiscussion, // discuss/sendDiscussion
	TaskOrMethod_DiscussDeleteDiscussion, // discuss/deleteDiscussion

	TaskOrMethod_FavoriteApply, // favorite/apply
	TaskOrMethod_FavoriteList, // favorite/list

	TaskOrMethod_CampusMlhsTypes, // campus/mlhs/types
	TaskOrMethod_MessageSendMsg, // message/sendMsg
	TaskOrMethod_MessageClassifiedMsgs, // message/classifiedMsgs
	TaskOrMethod_MessageListChat, // message/listChat
	TaskOrMethod_MessageListAdminMsgs, // message/listAdminMsgs
	TaskOrMethod_MessageUpdateMsgStatus, // message/updateMsgStatus

	TaskOrMethod_UserListFriends, // user/listFriends
	TaskOrMethod_UserListQuasiFriends, // user/listQuasiFriends
	TaskOrMethod_InformationListTypes, // information/listTypes

	TaskOrMethod_UserApprove, // user/approve
	TaskOrMethod_UserApply, // user/apply
	TaskOrMethod_UserSetProfile, // /user/setProfile
	TaskOrMethod_SettingGetAreaGroup, // setting/getAreaGroup
	TaskOrMethod_SettingGetDictionary, // setting/getDictionary

	TaskOrMethod_SurveyGetTopic, // survey/getTopic
	TaskOrMethod_SurveySubmit, // survey/submit

	TaskOrMethod_MessageListMsgs, // message/listMsgs

	// 入学办理
	TaskOrMethod_EnrolProgress, // enrol/progress
	TaskOrMethod_EnrolListqa, // enrol/listqa
	// TaskOrMethod_EnrolGetqa, //enrol/getqa

	// 课程表
	TaskOrMethod_CourseListCourse, // course/listCourse
	// report
	TaskOrMethod_ReportSaveReport, // report/saveReport

	// vote
	TaskOrMethod_VoteGetVoteTopic, // vote/getVoteTopic
	TaskOrMethod_VoteVoteSubmit, // vote/voteSubmit

	// store
	TaskOrMethod_MallHomeHome, // mallHome/home
	TaskOrMethod_GoodsTypeListGoodsType, // goodsType/listGoodsType
	TaskOrMethod_ShopShopData, // shop/shopData
	TaskOrMethod_GoodsListGoods, // goods/listGoods
	TaskOrMethod_GoodsGoodsData, // goods/goodsData
	TaskOrMethod_ShoppingCarSaveShoppingCar, // shoppingCar/saveShoppingCar
	TaskOrMethod_ShoppingCarListShoppingCar, // shoppingCar/listShoppingCar
	TaskOrMethod_OrdersSaveOrders, // orders/saveOrders
	TaskOrMethod_SecondHandandLost, // secondhandandlost/listSecond
	TaskOrMethod_SecondHandandLostDelete, // secondhandandlost/deleteSecond
	TaskOrMethod_SecondHandandLostPublish, // secondhandandlost/sendSecond
	TaskOrMethod_SecondHandandLostFinish, // secondhandandlost/finishSecond
	TaskOrMethod_SecondHandandLostDetails, // secondhandandlost/getSecond
	TaskOrMethod_RecruitServiceList, // recruit/listRecruit
	TaskOrMethod_RecruitServiceSend, // recruit/sendRecruit
	TaskOrMethod_RecruitServiceDelete, // recruit/deleteRecruit
	TaskOrMethod_RecruitServiceFinish, // recruit/finishRecruit
	TaskOrMethod_RecruitServiceDetails, // recruit/getRecruit
	TaskOrMethod_GetEnglishScore, // thirdparty/getCET
	TaskOrMethod_CommonServerInfo, // common/getCommonServer
	TaskOrMethod_CommonServerSubmit,//common/submitCommonServer	
	TaskOrMethod_UserAddressListUserAddress, //userAddress/listUserAddress
	TaskOrMethod_OrdersListOrders,//orders/listOrders
	TaskOrMethod_UserAddressSaveUserAddress,//userAddress/saveUserAddress
	TaskOrMethod_UserAddressUpdateUserAddress,//userAddress/updateUserAddress
	TaskOrMethod_UserAddressDelUserAddress,//userAddress/delUserAddress
	TaskOrMethod_ShopListShop,//shop/listShop
	TaskOrMethod_HotRecommendlistGoods,//hotRecommend/listGoods
	TaskOrMethod_ShoppingCarDelShoppingCar,//shoppingCar/delShoppingCar
	TaskOrMethod_Shop_ListShopType,//shop/listShopType
	TaskOrMethod_Goods_GoodsEvaluate,//goods/goodsEvaluate
	TaskOrMethod_GetWay,//getway
	TaskOrMethod_OrdersGetPayUrl,//orders/getPayUrl
	TaskOrMethod_OrdersEvaluateOrders,//orders/evaluateOrders
	TaskOrMethod_OrdersUpdateOrderStatus,//orders/updateOrderStatus
	TaskOrMethod_OneCardSolutionGetCard,//card/getCard
	TaskOrMethod_OneCardSolutionList,//card/getConsumeOrSave
	TaskOrMethod_OneCardSolutionReportLoss,//card/custLoss
	TaskOrMethod_CommonGetPayUrl,//common/getPayUrl	
	TaskOrMethod_ApplyCommunity,//community/applyCommunity
	TaskOrMethod_CommunityGetStructure,//community/getStructure
	TaskOrMethod_CommunityGetUserStructure,//getUserCommunity
	
	TaskOrMethod_AfterSaleService,//getUserCommunity
	
}