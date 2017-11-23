const functions = require('firebase-functions');

const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.profileInit = functions.auth.user().onCreate(event => {
  	
	const user = event.data;
	var userInfo = {
		displayName : user.displayName,
		email : user.email,
		createdOn : user.metadata.createdAt
	};
	admin.database().ref('users/' + user.uid + '/userInfo').set(userInfo);
});
