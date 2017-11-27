const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

/* 
	name: profileInit
	description: creates a user basic information node when a new user is authenticated

*/
exports.profileInit = functions.auth.user().onCreate(event => {
  	
	const user = event.data;
	var userInfo = {
		displayName : user.displayName,
		email : user.email,
		createdOn : user.metadata.createdAt
	};
	admin.database().ref('users/' + user.uid + '/userInfo').set(userInfo);
});


/*
	name: likeAchievementsChecker
	description: function that checks if the user is able to obtain a new like achievement.
		If he does the function adds the achievement to the user's achievements node.
*/
exports.likeAchievementsChecker = functions.database
.ref('users/{uid}/likedCombinations').onWrite(event => {
	
	const uid = event.params.uid;
	const numberOfLikes = event.data.numChildren();
	
	const getAchievementsPromise = admin.database().ref('achievements/likeAchievements')
	return  getAchievementsPromise.once('value').then(snapshot => {
		snapshot.forEach(achievement => {
			var parsedAchievement = achievement.val();

			if(numberOfLikes >= parsedAchievement['value']) {
				
				event.data.ref.parent.child('achievements').child(parsedAchievement['name']).set(parsedAchievement);
			} else {
				//console.log("not enough likes for next achievement");
			}
		});
	});
});


/*
	name: uploadAchievementsChecker
	description: function that checks if the user is able to obtain a new upload achievement.
		If he does the function adds the achievement to the user's achievements node.
*/
exports.uploadAchievementsChecker = functions.database
.ref('users/{uid}/uploadedCombinations').onWrite(event => {
	
	const uid = event.params.uid;
	const numberOfUploads = event.data.numChildren();
	
	const getAchievementsPromise = admin.database().ref('achievements/uploadAchievements')
	return  getAchievementsPromise.once('value').then(snapshot => {
		snapshot.forEach(achievement => {
			var parsedAchievement = achievement.val();

			if(numberOfUploads >= parsedAchievement['value']) {
				
				event.data.ref.parent.child('achievements').child(parsedAchievement['name']).set(parsedAchievement);
			} else {
				//console.log("not enough likes for next achievement");
			}
		});
	});
});

exports.componentAdding = functions.database.ref('combinations').onWrite(event => {
	
	const combination = event.data;
	const firstComponent = combination.val()['firstComponent'];
	const secondComponent = combination.val()['secondComponent'];

	event.data.ref.parent.child('components').child(firstComponent).set(secondComponent);
	event.data.ref.parent.child('components').child(secondComponent).set(firstComponent);

});




