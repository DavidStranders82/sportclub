
 //Datepicker script:

$(function() {
   $('#datetimepicker4').datepicker({
     format: 'dd/mm/yyyy'
   });
})

$(function() {
   $('#datetimepicker5').datepicker({
     format: 'dd/mm/yyyy'
   });
})

$(function() {
   $('#datetimepicker1').datetimepicker({
//     format: 'dd/mm/yyyy hh:mm:ss'
   });
})

// Delete member modal:
$(function() {
         $('.member-delete').click(function() {
              var memberId = $(this).data('memberId');
              var deleteLink = $('#closemodal');
              deleteLink.attr('href', '/admin/member/delete/' + memberId);
          });
})

// Delete team modal:
$(function() {
         $('.team-delete').click(function() {
              var teamId = $(this).data('teamId');
              var deleteLink = $('#closemodal');
              deleteLink.attr('href', '/admin/team/delete/' + teamId);
          });
})

// Delete competition modal:
$(function() {
         $('.competition-delete').click(function() {
              var competitionId = $(this).data('competitionId');
              var deleteLink = $('#closemodal');
              deleteLink.attr('href', '/admin/competition/delete/' + competitionId);
          });
})

// Delete location modal:
$(function() {
         $('.location-delete').click(function() {
              var locationId = $(this).data('locationId');
              var deleteLink = $('#closemodal');
              deleteLink.attr('href', '/admin/location/delete/' + locationId);
          });
})

// Delete game modal:
$(function() {
         $('.game-delete').click(function() {
              var gameId = $(this).data('gameId');
              var deleteLink = $('#closemodal');
              deleteLink.attr('href', '/admin/game/delete/' + gameId);
          });
})

// File uploading script:
$(function() {
    $(document).on('change', ':file', function() {
     var input = $(this),
     numFiles = input.get(0).files ? input.get(0).files.length : 1,
     label = input.val().replace(/\\/g, '/').replace(/.*\//, '');
    input.trigger('fileselect', [numFiles, label]);
   });

    $(document).ready( function() {
    $(':file').on('fileselect', function(event, numFiles, label) {
        var input = $(this).parents('.input-group').find(':text'),
        log = numFiles > 1 ? numFiles + ' files selected' : label;
         if( input.length ) {
            input.val(log);
        } else {
          if( log ) alert(log);
          }
       });
     });
});