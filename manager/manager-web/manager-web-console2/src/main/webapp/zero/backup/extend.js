/**
 * 
 */

Ext.override(Ext.data.AbstractStore,
{
	getUpdatedRecordsA : function() {
		return this.data.filterBy(this.filterUpdatedEx,this).items;
	},
	sync : function(options) {
		var me = this, operations = {}, toCreate = me.getNewRecords(), toUpdate = me.getUpdatedRecordsA(), toDestroy = me.getRemovedRecords(), needsSync = false;

		if (toCreate.length > 0) {
			operations.create = toCreate;
			console.log("Send Create", toCreate, this.data);
			needsSync = true;
		}
		
		if (toUpdate.length > 0) {
			console.log("Send UpdateData", toUpdate, this.data);
			operations.update = toUpdate;
			needsSync = true;
		}

		if (toDestroy.length > 0) {
			console.log("Send Destory", toDestroy, this.data);
			operations.destroy = toDestroy;
			needsSync = true;
		}

		if (needsSync && me.fireEvent('beforesync', operations) !== false) {
			options = options || {};
			console.log("Send Option",options);
            me.proxy.batch(Ext.apply(options, {
                operations: operations,
                listeners: me.getBatchListeners()
            }));
			//me.proxy.batch(options, me.getBatchListeners());
		}
	},
	getRemovedRecords: function() {
		console.log("getRemovedRecords",this);
		var delColumnFilter = function (item) {
			
			if(item.get('del_Yn')) {
				console.log("Del TRUE",this,item);
				return true;
			}
			console.log("Del FALSE",this,item);
			return false;
		};
		if(this.usedel) {
			return this.data.filterBy(delColumnFilter).items;
		}
		else {
			return this.removed;
		}
    },
	filterUpdatedEx : function(item , recordId , lvl) {
		var me = this;
		// 관계 데이터 변경 여부. 관계미존재시 false로 변경.
		var associationChange = true;
		if(lvl === undefined)  {
			lvl = 0;
		}
		else {
			lvl += 1;
		}
		// 관계데이터 존재여부 확인.
		var association = item['associations'];
		if (association && association.length > 0) {
			associationChange = false;
			
//			console.log("Item Has Association",item,association,association.length);
			for (var i = 0; i < association.length; i++) {
				var associationItem = association.items[i];
//				console.log("associationItem",associationItem);
				if (associationItem.type === 'hasOne') {
					if (item[associationItem.associationKey]) {
						var associationRecord = eval('item.' + associationItem.getterName + '()');
						associationChange = me.filterUpdatedEx(associationRecord);
					}
				} else {
//					console.log("HasMany Filter Check",me,this);
					if (item[associationItem.associationKey]) {
						var associationStore = eval('item.' + associationItem.storeName);
						// 자식 Store 변경 여부 Check
//						console.log("HasMany Child Store",item,associationStore);
						if(associationStore !== undefined) {
							associationStore.each(function(record) {
								associationChange = me.filterUpdatedEx(record,recordId,lvl);
								// 자식 Store 가 변경되었다면 Each Loop 종료.
								if (associationChange == true) {
									return true;
								}
							},this);
						}
					}
				}
				if (associationChange && item.phantom !== true) {
					item.setDirty();
					break;
				}
			}
		}
		else {
			console.log("Has No Association " + lvl);
			associationChange = false;
		}
		
		if(this.usedel && lvl === 0) {
			// 수정되었지만 삭제된 아이템으로 처리하기위해 Update대상에서는 제외.
			if(item.get('del_Yn')) {
				console.log("DEL YN USE It's Removed Row",item);
				return false;
			}
		}
		
		// 자식 데이터가 변경된 경우 해당 Record 변경 여부와 상관없이 변경 데이터로 처리.
		if(item.phantom !== true && item.isValid() && associationChange) {
			console.log("Top Node is Update " + lvl +"?",(item.phantom !== true && item.isValid() && associationChange));
			return true;
		}
		// 부모가 존재하는경우!!!!! phantom인건도 포함되어야 하지만
		// 자신이 최상위 부모인 경우에는 phantom은 제외되어야함.
		console.log("Filter Update Result 1 " + lvl,(item.dirty === true || item.phantom === true) && item.isValid(),"VALS",item.dirty, item.phantom,item.isValid(),associationChange,item);
		if(lvl > 0) {
			return (item.dirty === true || item.phantom === true) && item.isValid();
		}
		else {
			console.log("Top Node is Update 2 " + lvl +"?",(item.phantom !== true && item.dirty === true && item.isValid()) );
			if( item.phantom !== true && item.dirty === true && item.isValid()) {
				return true;
			}
		}
	}
});

Ext.override(Ext.form.field.Radio, {
	resetOriginalValue : function() {
		// Override the original method inherited from Ext.form.field.Field:
		// this.originalValue = this.getValue();
		// this.checkDirty();
		console.log("resetOriginalValue : ", this, this.getManager(), this.getManager().getByName(this.name));
		var radioName = this.name;
		var radioformId = this.formId;
		this.getManager().filterBy(function(item) {
			console.log("RadioManager FilterBy : ", item, item.name === radioName);
			return item.name === radioName && item.formId === radioformId;

		}).each(function(item) {
			console.log("Set Radio Value", item);
			item.originalValue = item.getValue();
			item.checkDirty();
		});
	},
});

Ext.override(Ext.form.Basic, {
	findAssosiationForm : function(key) {
		return this.owner.items.findBy(function(f) {
			// console.log("Find by : ",f)
			return f.xtype === "subrecordform" && f.name === key;
		});
	},

	findAssosiationGrid : function(key) {
		return this.owner.items.findBy(function(f) {
			// console.log("Find by : ",f)
			return f.xtype === "subrecordgrid" && f.name === key;
		});
	},

	// Form Data 반영.
	updateRecord : function(record) {

		console.log("Override Ext.form.Basic updateRecord");
		// 기존의 로직 사용.
		this.callParent([ record ]);
		record = record || this._record;

		// Association Model을 갖는 Model인경우 하위 관계 모델에 대한 처리
		if (record['associations'] && record['associations'].length > 0) {

			// 관게 모델 목록을 순회
			for (key in record.associations.items) {
				var associationItem = record.associations.items[key];

				// 단일건 매치관계의 경우에만 처리. 그리드는 다른방식으로 접근.
				if (associationItem.type === 'hasOne') {
					// subrecordform Type의 Child
					var subForm = this.findAssosiationForm(associationItem.name);
					if (subForm) {
						// 해당 관계 데이터가 존재하지 않을경우.. 만들어내야함.
						console.log("Update SubForm : ", this, subForm.getForm());
						var updatedRecord = null;
						if (subForm.getForm()['_record']) {
							subForm.updateRecord();
						} else {
							console.log("Has No Record Will Create");
							var newRecord = associationItem.associatedModel.create();
							subForm.updateRecord(newRecord);

							eval('record.' + associationItem.setterName + '(newRecord);');
						}
					} else {
						console.log("SubForm Not Exists");
					}

				} else if (associationItem.type === 'hasMany') {
					var subGrid = this.findAssosiationGrid(associationItem.name);
					if (subGrid) {
						// 해당 관계 데이터가 존재하지 않을경우.. 만들어내야함.
						console.log("Update SubGrid : ", subGrid.getStore().getModifiedRecords());
						subGrid.saveState();

					}
				}
			}
		}

		console.log("Update Finish");
	},
	// Tree Structure Data Load
	// 신규 Row 생성시 관계형 자식 Store와 함께 생성.
	loadRecord : function(record) {
		var me = this;
		console.log("Override Ext.form.Basic loadRecord");
		this.callParent([ record ]);

		// 수신데이터를 기준으로 SubForm에 해당 Data Bind
		if (record['associations']) {
			for (key in record.associations.items) {
				var associationItem = record.associations.items[key];
				// 단일건 매치관계의 경우에만 처리. 그리드는 다른방식으로 접근.
				if (associationItem.type === 'hasOne') {
					// 
					var subForm = this.findAssosiationForm(associationItem.associationKey);
					if (subForm) {
						subForm.getForm().reset();
						// 해당 관계 데이터가 존재하지 않을경우. Rercord 바인딩 처리하지 않음.
						if (record[associationItem.associationKey]) {
							var associationRecord = eval('record.' + associationItem.getterName + '()');

							if (associationRecord) {
								console.log("Find Sub Form", associationRecord);
								subForm.getForm().loadRecord(associationRecord);
							}
						} else {
							console.log("Empty Record Reset Form", subForm, record);
						}
						subForm.getForm().wasDirty = false;
						console.log("DIRTY ? ", subForm.getForm());

					}
				} else if (associationItem.type === 'hasMany') {
					var subGrid = this.findAssosiationGrid(associationItem.associationKey);
					if (subGrid) {
						console.log("Sub Grid LoadRecords");
						if (record[associationItem.associationKey]) {
							var associationStore = eval('record.' + associationItem.storeName);
							subGrid.reconfigure(associationStore);
						}
					}
				}
			}
		}
		Ext.each(this.getFields().items, function(val) {
			console.log("Fields", val.$className, val.isDirty(), val.name);
		});

		return this;
	}
});

// Json Writer Override
Ext.override(Ext.data.writer.Json, {
	getRecordData : function(record) {
		var me = this , childStore, data = {};
		console.log("Override Ext.data.writer.Json getRecordData", record);
		data = me.callParent([ record ]);

		// 자식노드가 존재한다면 모든 자식노드의 데이터를 순회하며 처리.
		for (var i = 0; i < record.associations.length; i++) {
			var associationItem = record.associations.get(i);
			// 1:N 관계의 자식관계일 경우 처리
			if (associationItem.type == 'hasMany') {
				// 처리결과를 배열로 담는다.
				data[associationItem.name] = [];
				// 해당
				childStore = eval('record.' + associationItem.storeName);
				if(childStore !== undefined) {
					childStore.each(function(childRecord) {
						// Recursively get the record data for children (depth
						// first)
						var childData = this.getRecordData.call(this, childRecord);
						if (childRecord.dirty | childRecord.phantom | (childData != null)) {
							data[associationItem.associationKey].push(childData);
							record.setDirty();
						}
					}, me);
				}
			} else if (associationItem.type == 'hasOne') {
				data[associationItem.associationKey] = null;
				if (record[associationItem.name]) {
					childRecord = eval('record.' + associationItem.getterName + '()');
					var childData = this.getRecordData.call(this, childRecord);
					if (childRecord.dirty | childRecord.phantom | (childData != null)) {
						data[associationItem.associationKey] = childData;
						record.setDirty();
					}
				}

			}

		}
		return data;
	}
});
