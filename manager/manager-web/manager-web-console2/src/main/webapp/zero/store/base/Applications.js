/**
 * 서버 관리 뷰
 */
Ext.define('Zero.store.base.Applications', {
	extend : 'Zero.ex.store.ZStore',
	storeId:'base_Applications',
	pageSize : 10,
	//model : 'Zero.model.base.Application',
	fields : [ 'del_Yn','app_Id', 'app_Name', 'app_Processor_Name', 'app_Ver', 'cluster_Yn'],
	constructor : function () {
		var me = this;
		me.callParent(arguments);
		Ext.apply(me.proxy,{
			api : {
				read : '/console2/application/list.json',
				update : '/console2/application/modify.json',
				create : '/console2/application/add.json',
				destroy : '/console2/application/delete.json'
			}
		});
	}
});