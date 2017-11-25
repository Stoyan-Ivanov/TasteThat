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

exports.likeAchievementsChecker = functions.database
.ref('users/{uid}/likedCombinations').onWrite(event => {
	
	const uid = event.params.uid;
	const numberOfLikes = event.data.numChildren();
	
	const getAchievementsPromise = admin.database().ref('likeAchievements')
	return  getAchievementsPromise.once('value').then(snapshot => {
		snapshot.forEach(achievement => {
			var parsedAchievement = achievement.val();

			if(numberOfLikes >= parsedAchievement['value']) {
				
				event.data.ref.parent.child('achievements').child(parsedAchievement['name']).set(parsedAchievement);
			} else {
				console.log("not enough likes for next achievement");
			}
		});
	});
});
