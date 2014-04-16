/**
 * 
 */
Ext.define('Zero.store.install.Nodes', {
	extend : 'Ext.data.Store',
	storeId: 'install_nodes',
	fields: ['node_id'
		   , 'node_name'
		   , 'node_addr'
		   , 'node_user'
		   , 'node_password'
		   , 'node_home'
		   , 'bhadoop'
		   , 'bhive'
		   , 'bhbase'
		   , 'bzk']	,
	autoLoad : false,
	listeners: {
	    'load': function(store, records, successful, operation) {
	      console.log("Loaded" , store, records , operation , successful);
	      console.log("0 Row Data : " ,store.getAt(0));
	      
	    }
	  },
	pageSize: 10,
	proxy : {
		type : 'ajax',
		url : '/console2/node',
		reader : {
			type : 'json',
			root : '_rslt',
		}
	}
});