/**
 * 
 */
Ext.define('Zero.store.base.InstallStatus', {
	extend : 'Ext.data.Store',
	storeId : 'installStatus',
	//fields : [ 'group', 'id', 'name', 'view', 'leaf' ],
	fields : [ 'code', 'name'],
	autoLoad : false,
	autoSync : false,
	data : [
	// {group: 'base', name: '서버' , left:false},
	{
		//id : 'T_SERVERGROUP',
		code : 'N',
		name : '미설치',
	}, {
//		id : 'T_SERVER',
		code : 'E',
		name : '설치오류',
	},
	// {group: 'base', name: '어플리케이션' , left:false},
	{
//		id : 'T_APPMANAGE',
		code : 'P',
		name : '설치진행중',
	}, {
//		id : 'T_APPHADOOP',
		code : 'F',
		name : '설치완료',
	} ]

});