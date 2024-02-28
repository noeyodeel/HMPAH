$(document).ready(function () {
  const token = Cookies.get('Authorization');
  //페이지 로드시 nickname 가져오기
  getUserInfo();

  // 페이지 로드 시 게시글 목록 가져오기
  getPostList();

  //nickname 가져오기
  function getUserInfo() {
    $.ajax({
      type: "GET",
      url: "/profiles",
      headers: {
        Authorization: token
      },
      success: function (user) {
        const nickname = user.nickname;
        $('#nickname').text(nickname);
      },
      error: function (xhr, status, error) {
        console.error(error);
        alert("사용자 정보를 가져오는데 실패했습니다.");
      }
    });
  }

  //로그아웃
  $('#logout-btn').on('click', function () {
    Cookies.remove('Authorization', {path: '/'});
    window.location.href = '/users/login-page';
  });


  function getPostList() {
    // 게시글 목록 가져오는 AJAX 요청
    $.ajax({
      type: "GET",
      url: "/posts",
      headers: {
        Authorization: token
      },
      success: function (response) {
        displayPosts(response);
      },
      error: function (xhr, status, error) {
        console.error(error);
        alert("게시글 목록을 불러오는데 실패했습니다.");
      }
    });
  }

// 게시글을 화면에 출력하는 함수
  function displayPosts(posts) {
    $('#post-list').empty();
    $('#comment-list').empty(); // 기존의 댓글 목록을 지움
    posts.forEach(function (post) {
      const card = `
            <li class="card" data-post-id="${post.id}">
                <h1>[${post.id}] ${post.title}</h1>
                <h3>${post.location}</h3>
                <p>작성자: ${post.nickname}</p>
                <p>${post.content}</p>
                <p>좋아요 : ${post.likescnt}</p>
                <p>${post.status} ( ${post.currentCount}명 /  ${post.maxCount}명 )</p>
            </li>`;
      $('#post-list').append(card);
    });

    // 각 게시글에 클릭 이벤트 추가
    $('.card').on('click', function () {
      const postId = $(this).data('post-id');
      getPostById(postId);
    });
  }

  // 특정 게시글의 상세 정보를 가져와서 출력하는 함수
  function getPostById(postId) {
    $.ajax({
      type: "GET",
      url: "/posts/" + postId,
      headers: {
        Authorization: token
      },
      success: function (post) {
        displayPostDetail(post);
        getComments(postId); // 상세 정보를 가져온 후에 댓글을 가져옴
      },
      error: function (xhr, status, error) {
        console.error(error);
        alert("게시글을 가져오는데 실패했습니다.");
      }
    });
  }

  // 특정 게시글의 상세 정보를 화면에 출력하는 함수
  function displayPostDetail(post) {
    $('#post-list').empty();
    const card = `
    <li class="card">
        <p><button id="update-post-btn">게시글 수정</button></p>
        <p><button id="delete-post-btn">게시글 삭제</button></p>
        <h1>[${post.id}] ${post.title}</h1>
        <h3>지역 : ${post.location}</h3>
        <p>작성자 : ${post.nickname}<button id="follow-btn">팔로우</button></p>
        <p>${post.content}</p>
        <p>좋아요: <span id="like-count">${post.likescnt}</span> <button id="like-btn">좋아요</button></p>
        <p>${post.status} ( ${post.currentCount}명 /  ${post.maxCount}명 )<button id="join-btn">참가하기</button></p>
    </li>`;
    $('#post-list').append(card);

    // 게시글 작성 버튼 클릭 시 게시글 작성 폼을 표시
    $('#update-post-btn').on('click', function () {
      const postNum = post.id;
      // 게시글 작성 폼을 보여줄 HTML을 작성
      const postForm = `
    <form id="post-update-form">
        <label for="update-title">제목:</label><br>
        <input type="text" id="update-title" name="update-title"><br>
        <label for="update-location">지역:</label><br>
        <input type="text" id="update-location" name="update-location"><br><br>
        <label for="update-content">내용:</label><br>
        <textarea id="update-content" name="update-content"></textarea><br>
        <label for="update-maxcount">모집인원:</label><br>
        <textarea id="update-maxcount" name="update-maxcount"></textarea><br>
        <button type="submit">수정 완료</button>
    </form>`;

      // 게시글 작성 폼을 표시할 위치에 추가
      $('#post-list').prepend(postForm);

      // 게시글 작성 폼이 제출되면 submitPost 함수 호출
      $('#post-update-form').on('submit', function (event) {
        event.preventDefault(); // 폼 제출 시 페이지 새로고침 방지
        const postUpdateData = {
          title: $('#update-title').val(), // 변경된 부분
          content: $('#update-content').val(),
          location: $('#update-location').val(),
          maxcount: $('#update-maxcount').val()
        };
        updatePost(postNum, postUpdateData);
      });
    });

    // 댓글 입력 폼 추가
    const commentForm = `
    <form id="comment-form">
        <label for="comment-content">댓글 작성:</label><br>
        <textarea id="comment-content" name="comment-content"></textarea><br>
        <button type="button" id="submit-comment">댓글 작성</button>
    </form>`;
    $('#post-list').append(commentForm);

    // 댓글 작성 버튼에 클릭 이벤트 핸들러 등록
    $('#submit-comment').on('click', function () {
      const commentContent = $('#comment-content').val();
      const postId = post.id; // 현재 게시글의 ID 가져오기

      if (commentContent.trim() !== '') {
        const commentData = {
          postId : postId,
          content: commentContent
        };
        submitComment(commentData);
      }
    });
    $('#like-btn').on('click', function () {
      submitLike(post.id);
    });

    $('#delete-post-btn').on('click', function () {
      deletePost(post.id);
    });

    // 팔로우 버튼에 클릭 이벤트 핸들러 등록
    $('#follow-btn').on('click', function () {
      submitFollow(post.userId);
    });

    $('#join-btn').on('click', function () {
      submitJoin(post.id);
    });
  }

  function getComments(postId) {
    $.ajax({
      type: "GET",
      url: "/comments/"+ postId,
      headers: {
        Authorization: token
      },
      success: function (response) {
        displayComments(response, postId);
      },
      error: function (xhr, status, error) {
        console.error(error);
        alert("댓글을 가져오는데 실패했습니다.");
      }
    });
  }

  // 댓글을 화면에 출력하는 함수
  function displayComments(comments, postId) {
    $('#comment-list').empty();
    comments.forEach(function (comment) {
      const card = `
        <li class="card" data-comment-id="${comment.id}">
            <p><button class="delete-comment-btn" data-comment-id="${comment.id}">댓글 삭제</button><button class="update-comment-btn" data-comment-id="${comment.id}">댓글 수정</button></p>
            <p>작성자: ${comment.nickname} <button class="follow-btn" data-nickname="${comment.nickname}">팔로우</button></p>
            <p>${comment.content}</p>
            <p>좋아요: <span id="like-count-${comment.id}">${comment.likescnt}</span> <button class="comment-like-btn" data-comment-id="${comment.id}">좋아요</button></p>
            
        </li>`;
      $('#comment-list').append(card);
    });

    $('.comment-like-btn').on('click', function () {
      const commentId = $(this).data('comment-id');
      const commentLikeRequest = {
        commentId: commentId
      };
      submitCommentLike(commentLikeRequest, postId);
    });

    $('.delete-comment-btn').on('click', function () {
      const commentId = $(this).data('comment-id');
      deleteComment(commentId, postId);
    });

    $('.update-comment-btn').on('click', function () {
      const commentId = $(this).data('comment-id');
      const postNum = postId;
      const commentForm = `
    <form id="comment-form">
        <label for="comment-content">댓글 수정:</label><br>
        <textarea id="update-comment-content" name="update-comment-content"></textarea><br>
        <button type="button" id="submit-update-comment">댓글 수정</button>
    </form>`;
      $('#comment-list').append(commentForm);

      // 댓글 수정 버튼에 클릭 이벤트 핸들러 등록
      $('#submit-update-comment').on('click', function () {
        const commentContent = $('#update-comment-content').val();
        const postId = postNum; // 현재 게시글의 ID 가져오기

        if (commentContent.trim() !== '') {
          const commentData = {
            postId : postId,
            content: commentContent
          };
          updateComment(postId, commentId, commentData);
        }
      });
    });

    // 댓글 작성자를 팔로우하는 버튼에 클릭 이벤트 핸들러 등록
    $('.follow-btn').on('click', function () {
      const userId = $(this).data('userId');
      submitFollow(userId);
    });
  }

  function submitCommentLike(commentId, postId) {
    $.ajax({
      type: "POST",
      url: `/comments/likes/` + commentId,
      headers: {
        Authorization: token
      },
      contentType: 'application/json',
      success: function (response) {
        alert(response);
        getPostById(postId);
      },
      error: function (xhr, status, error) {
        console.error(error);
        alert("좋아요에 실패했습니다.");
      }
    });
  }

  function updateComment(postId, commentId, commentData) {
    $.ajax({
      type: "PUT",
      url: `/comments/` + commentId,
      headers: {
        Authorization: token
      },
      contentType: 'application/json',
      data: JSON.stringify(commentData),
      success: function (response) {
        alert("댓글이 수정되었습니다.");
        // 댓글 작성 완료 후 댓글 목록 다시 불러오기
        getComments(postId);
      },
      error: function (xhr, status, error) {
        console.error(error);
        alert("댓글 수정에 실패했습니다.");
      }
    });
  }

  function deleteComment(commentId, postId) {
    $.ajax({
      type: "DELETE",
      url: `/comments/` + commentId,
      headers: {
        Authorization: token
      },
      contentType: 'application/json',
      success: function (response) {
        alert(response);
        getPostById(postId);
      },
      error: function (xhr, status, error) {
        console.error(error);
        alert("삭제에 실패했습니다.");
      }
    });
  }

// 댓글을 서버에 전송하는 함수
  function submitComment(commentData) {
    $.ajax({
      type: "POST",
      url: `/comments`,
      headers: {
        Authorization: token
      },
      contentType: 'application/json',
      data: JSON.stringify(commentData),
      success: function (response) {
        alert("댓글이 작성되었습니다.");
        // 댓글 작성 완료 후 댓글 목록 다시 불러오기
        getComments(postId);
      },
      error: function (xhr, status, error) {
        console.error(error);
        alert("댓글 작성에 실패했습니다.");
      }
    });
  }

  function submitFollow(userId) {
    $.ajax({
      type: "POST",
      url: `/followings/` + userId,
      headers: {
        Authorization: token
      },
      contentType: 'application/json',
      success: function (response) {
        alert(response);
        getPostById(postId)
      },
      error: function (xhr, status, error) {
        console.error(error);
        alert("팔로우에 실패했습니다.");
      }
    });
  }

  function submitJoin(postId) {
    $.ajax({
      type: "POST",
      url: `/posts/` + postId + '/joins',
      headers: {
        Authorization: token
      },
      contentType: 'application/json',
      success: function (response) {
        alert(response);
        getPostById(postId)
      },
      error: function (xhr, status, error) {
        console.error(error);
        alert("게시글 참여에 실패했습니다.");
      }
    });
  }

  function submitCommentLike(commentLikeRequest, postId) {
    $.ajax({
      type: "POST",
      url: `/comments/likes/`,
      headers: {
        Authorization: token
      },
      contentType: 'application/json',
      data: JSON.stringify(commentLikeRequest),
      success: function (response) {
        alert(response);
        getPostById(postId);
      },
      error: function (xhr, status, error) {
        console.error(error);
        alert("좋아요에 실패했습니다.");
      }
    });
  }

  // 게시글 작성 요청을 서버에 전송하는 함수
  function updatePost(postId, postUpdateData) {
    $.ajax({
      type: "PUT",
      url: "/posts/" + postId,
      headers: {
        Authorization: token
      },
      contentType: 'application/json',
      data: JSON.stringify(postUpdateData),
      success: function (response) {
        alert("게시글이 수정되었습니다.");
        // 작성 완료 후 게시글 목록 다시 불러오기
        getPostById(postId);
      },
      error: function (xhr, status, error) {
        console.error(error);
        alert("게시글 수정에 실패했습니다.");
        getPostById(postId);
      }
    });
  }

  function submitLike(postId) {
    $.ajax({
      type: "POST",
      url: `/posts/` + postId + '/likes',
      headers: {
        Authorization: token
      },
      contentType: 'application/json',
      success: function (response) {
        alert(response);
        getPostById(postId)
      },
      error: function (xhr, status, error) {
        console.error(error);
        alert("좋아요에 실패했습니다.");
      }
    });
  }

  function deletePost(postId) {
    $.ajax({
      type: "DELETE",
      url: `/posts/` + postId,
      headers: {
        Authorization: token
      },
      contentType: 'application/json',
      success: function (response) {
        alert(response);
        getPostList()
      },
      error: function (xhr, status, error) {
        console.error(error);
        alert("게시글 삭제에 실패했습니다.");
      }
    });
  }

  function createPost(){
    console.log("실행됨");
    const edite =
    `<li class="card" data-post-id="${post.id}">
      <h1>[${post.id}] ${post.title}</h1>
      <h3>${post.location}</h3>
      <p>작성자: ${post.nickname}</p>
      <p>${post.content}</p>
      <p>좋아요 : ${post.likescnt}</p>
      <p>${post.status} ( ${post.currentCount}명 /  ${post.maxCount}명 )</p>
    </li>`;

    $('#post-list').append(edite);
  }
});