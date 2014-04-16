/**
 * Grid 내 CheckBox Delete용도의 컴포넌트.
 * 서버 VO에서 DEL_YN컬럼 없이 처리하기 위한 VO
 * Tree 구조의 Data일 경우 DELETE 케이스 처리 방안 수립후 재시도.  
 */
Ext.define('Zero.ex.store.ZStore', {
	extend : 'Ext.data.Store',
	usedel : true,
	constructor : function() {
		var me = this;
		Ext.apply(me,{
			proxy : {
				headers : {
					'accept' : 'application/json'
				},
				type : 'ajax',
				writer : {
					type : 'json',
					allowSingle : false,
					root : 'list'
				},
				reader : {
					type : 'json',
					root : '_rslt',
				},
				listeners : {
					exception : function(proxy, response, operation) {
						console.log(operation.getError());
						Ext.MessageBox.show({
							title : 'REMOTE EXCEPTION',
							msg : operation.getError().statusText,
							icon : Ext.MessageBox.ERROR,
							buttons : Ext.Msg.OK
						});
					}
				}
			}
		});
		me.callParent(arguments);
		me.on({
			write : {
				fn : me.destorySuccess,
				scope : me
			},
		});
		console.log('ZStore Event', me.events);
	},
	destorySuccess : function(proxy, operation) {
		var me = this;
		console.log("DestorySuccess", arguments);

		var delColumnFilter = function(item) {
			console.log("Del Flag", this, item);
			if (item.get('del_Yn')) {
				return true;
			}
			return false;
		};

		if (operation.action == 'destroy') {

			if (this.usedel) {
				// 삭제 완료건 그리드에서 제거
				Ext.each(this.data.filterBy(delColumnFilter).items, function(item) {
					me.remove(item);
				});
				// 삭제 상태 제거
				me.removed.length = 0;
				me.commitChanges();
			}
		} else {
			//삭제 및 변경 건 Local Commit처리.
			function removeDelColumn(records, store) {
				console.log("RemoveColumns Call", records);
				Ext.each(records, function(record) {
					console.log("Loop Records", record);
					for ( var x in record['substores']) {
						var storeInfo = record['substores'][x];
						console.log("Loop Records Store", storeInfo);
						Ext.each(record[storeInfo.storeName].data.items, function(item) {
							removeDelColumn(item, record[storeInfo.storeName]);
						});
					}
					if (record.get('del_Yn')) {
						console.log("Remove Row", record);
						store.remove(record);
						return true;
					}
				});
				store.commitChanges();
				console.log("STORE COMMIT", store);
			}
			if (this.usedel) {
				removeDelColumn(operation.records, this);
			}
		}
	},
	listeners : {
		write : function(proxy, operation) {
			Ext.MessageBox.show({
				title : '완료.',
				msg : '저장을 완료 하였습니다.',
				icon : Ext.MessageBox.OK,
				buttons : Ext.Msg.OK
			});
		}
	}
});