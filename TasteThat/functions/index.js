const functions = require('firebase-functions');

// // Create and Deploy Your First Cloud Functions
// // https://firebase.google.com/docs/functions/write-firebase-functions
//
// exports.helloWorld = functions.https.onRequest((request, response) => {
//  response.send("Hello from Firebase!");
// });
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
.ref('users/{uid}/ratedCombinations').onWrite(event => {
	
	const uid = event.params.uid;
	const numberOfLikes = event.data.numChildren();
	
	const getAchievementsPromise = admin.database().ref('achievements/ratingAchievements')
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

/*
	name: onRatingSubmitted
	description: function that calculates the average rating of a combination.
*/
exports.onRatingSubmitted = functions.database.ref('combination_ratings/{pushId}/users').onWrite(event => {

	const pushId = event.params.pushId;
	const numberOfRates =  event.data.numChildren();
	console.log("rates: " + numberOfRates);

	return admin.database().ref('combination_ratings/' + pushId +'/users').once('value').then( snapshot => {
		var rating = 0;
		var users = [];

		snapshot.forEach(combination_rating => {
			rating += combination_rating.val();
			users.push(combination_rating.key);
			console.log("current rating :" + rating);
		});
		updateRatingField(rating / numberOfRates, pushId, users);

	});
});

function updateRatingField(average_rating, pushId, users) {
	admin.database().ref('combination_ratings').child(pushId).child("rating").set(average_rating);
	admin.database().ref('combinations').child(pushId).child("rating").set(average_rating);
	admin.database().ref('combinations').child(pushId).child("negativeRating").set(0 - average_rating);
}


