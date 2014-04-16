/**
 * 
 */

Ext.define('Zero.store.manage.ServerApplications', {
	extend : 'Zero.ex.store.ZStore',
	storeId : 'manage_serverApplications',
	fields : [ 'del_Yn', 'server_Id', 'server_Name', 'host_Info' , 'user_Id' , 'user_Pwd' , 'group_Id'],
	pageSize : 10,
	proxy : {
		headers : {
			'accept' : 'application/json'
		},
		type : 'ajax',
		api : {
			read : '/console2/server/list.json',
			update : '/console2/server/modify.json',
			create : '/console2/server/add.json',
			destroy : '/console2/server/delete.json'
		},
		writer : {
			type : 'json',
			allowSingle :false,
			root : 'list'
		},
		reader : {
			type : 'json',
			root : '_rslt',
		},
		listeners: {
            exception: function(proxy, response, operation){
            	console.log(operation.getError());
                Ext.MessageBox.show({
                    title: 'REMOTE EXCEPTION',
                    msg: operation.getError().statusText,
                    icon: Ext.MessageBox.ERROR,
                    buttons: Ext.Msg.OK
                });
            }
        }
	}
});