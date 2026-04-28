<!DOCTYPE html>
<html lang="en" class="h-full">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Positions - HR Admin</title>

    <!-- Favicon -->
    <link rel="icon" href="/hr_admin/favicon.ico" type="image/x-icon">
    <link rel="shortcut icon" href="/hr_admin/favicon.ico" type="image/x-icon">

    <!-- Tailwind CSS via CDN -->
    <!--<script src="https://cdn.tailwindcss.com"></script>-->
    <script src="${springMacroRequestContext.contextPath}/js/tailwind.js"></script>

    <!-- Alpine.js for interactivity -->
    <!--<script src="https://unpkg.com/alpinejs@3.x.x/dist/cdn.min.js" defer></script>-->
    <script src="${springMacroRequestContext.contextPath}/js/cdn.min.js" defer></script>

    <!-- Inter Font -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">

    <link href="${springMacroRequestContext.contextPath}/css/hr_admin.css" rel="stylesheet">
    <style>
        /* Стили для модального окна Удаления */
        #deleteEmployeeModal {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background-color: rgba(0, 0, 0, 0.5);
            z-index: 9999;
            align-items: center;
            justify-content: center;
        }
        .modal-container {
            background-color: white;
            border-radius: 0rem;
            box-shadow: 0 10px 15px -3px rgba(0, 0, 0, 0.1), 0 4px 6px -2px rgba(0, 0, 0, 0.05);
            padding: 1.5rem;
            width: 90%;
            max-width: 400px;
            transform: scale(0.9);
            opacity: 0;
            transition: all 0.2s ease-out;
        }
        #deleteEmployeeModal.show .modal-container {
            transform: scale(1);
            opacity: 1;
        }
        .modal-title {
            font-size: 1.25rem;
            font-weight: 600;
            color: #0a3d62;
            margin-bottom: 0.5rem;
            display: flex;
            align-items: center;
            gap: 0.5rem;
        }
        .modal-actions {
            display: flex;
            justify-content: flex-end;
            gap: 0.75rem;
            margin-top: 1.5rem;
        }
        .btn-cancel {
            padding: 0.5rem 1rem;
            background-color: #e2e8f0;
            color: #2d3748;
            border: none;
            border-radius: 0 rem;
            font-size: 0.875rem;
            cursor: pointer;
            transition: background-color 0.2s;
        }
        .btn-cancel:hover {
            background-color: #cbd5e0;
        }
        .btn-delete {
            padding: 0.5rem 1rem;
            background-color: #e53e3e;
            color: white;
            border: none;
            border-radius: 0rem;
            font-size: 0.875rem;
            cursor: pointer;
            transition: background-color 0.2s;
        }
        .btn-delete:hover {
            background-color: #c53030;
        }
        .warning-icon {
            width: 1.5rem;
            height: 1.5rem;
            color: #dd6b20;
        }
    </style>
</head>

<body class="bg-gray-50 min-h-screen flex">
    <!-- Show positions page -->
    <!-- Include Sidebar Fragment -->
    <#include "fragments/sidebar.ftl">
    <!-- End Include Sidebar Fragment -->

    <!-- Main Content -->
    <div class="flex flex-1 flex-col overflow-hidden">
        <!-- Header -->
        <div class="w-full mx-auto bg-white shadow-xl border border-gray-100 text-left">
            <div class="bg-sky-900 px-6 py-3 logo-padding text-white text-left flex flex-wrap items-center justify-between gap-4">
                <h2 class="font-semibold px-2 " style="font-size: 1.5rem; line-height: 2rem;">Должности</h2>

                <div class="flex  mobile-sort-container justify-between items-center gap-2">
                    <a href="${springMacroRequestContext.contextPath}/positions/new"
                       class="btn-dark-blue mobile-add-btn px-5 py-2 font-semibold shadow transition flex items-center gap-2 rounded-none hover:bg-sky-50 hover:text-sky-900 whitespace-nowrap "
                    >
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
                        </svg>
                        Добавить
                    </a>
                </div>
            </div>

            <div class="p-6 overflow-x-auto">
                <table class="w-full text-sm text-left text-gray-700 table-auto">
                    <!-- Caption -->
                    <thead class="uppercase border-t border-gray-100 bg-gray-50">
                        <!-- Name -->
                        <th class="px-6 py-09rem cursor-pointer group font-medium font-black">Должность</th>
                        <!-- Actions -->
                        <th class="px-6 py-09rem cursor-pointer group font-medium font-black">Действия</th>
                    </thead> <!-- Caption -->
                    <tbody>
                        <!-- Position Details -->
                        <#list positions as position>
                            <tr class="border-b border-gray-100 hover:bg-gray-50 transition text-base">
                                <td class="px-6 py-05rem">${position.name} (${position.id})</td>
                                <td class="px-6 flex justify-left gap-2 py-05rem">
                                    <a href="${springMacroRequestContext.contextPath}/positions/edit/${position.id}"
                                        class="action-edit inline-flex items-center justify-center w-8 h-8 rounded-full hover:bg-sky-100 transition"
                                        title="Изменить">
                                        <svg class="action-icon" xmlns="https://www.w3.org/2000/svg" fill="none" viewBox="-4 -4 30 30" stroke="currentColor">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"/>
                                        </svg>
                                    </a>
                                    <!-- Delete Position with Confirmation -->
                                    <!-- onclick="openDeleteModal(${position.id})" -->
                                    <!-- ID ???? При фиксированном ID работает !!!! -->
                                    <button type="button"
                                        onclick="openDeleteModal(${position.id})"
                                        title="Удалить должность"
                                        class="text-sky-900 hover:text-sky-700 hover:bg-red-100 font-medium text-sm transition flex items-center gap-1">
                                        <svg xmlns="http://www.w3.org/2000/svg" class="h-8 w-8 text-red-600" fill="none" viewBox="-4 -4 30 30" stroke="currentColor">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                                        </svg>
                                    </button>
                                </td>
                            </tr>
                        </#list>
                    </tbody>
                </table>
            </div>
        </div> <!-- HEADER -->
    </div> <!-- Main Content -->

    <!-- Confirmation Modal -->
    <div id="deleteModal">
        <div class="modal-container">
            <div class="modal-title">
                <svg class="warning-icon" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
                </svg>
                Подтверждение удаления
            </div>
            <p>Вы уверены, что хотите удалить <span id="deleteName" class="font-medium"></span>?</p>
            <div class="modal-actions">
                <button type="button" onclick="closeDeleteModal()" class="btn-cancel">Отмена</button>
                <!-- РАБОТАЕТ УДАЛЕНИЕ, ДИАЛОГ ЗАКРЫВАЕТСЯ. НО НЕ ОБНОВЛЯЕТСЯ. И где взять ID ?-->
                <button id="confirmDeleteBtn" type="button" onclick="deletePosition(4)" class="btn-delete">Удалить</button>
                <!-- НЕ РАБОТАЕТ УДАЛЕНИЕ, ДИАЛОГ ЗАКРЫВАЕТСЯ, ОБНОВЛЯЕТСЯ. НУЖНО ТОЛЬКО УСТАНОВИТЬ ID -->
                <a id='link_delete_a' class="btn-delete" href="${springMacroRequestContext.contextPath}/positions/delete/4">Delete</a>
            </div>
        </div>
    </div>
    <script>
        async function openDeleteModal(id) {
            // url - ${springMacroRequestContext.contextPath}/positions
            console.log("openDeleteModal: id=" + id);
            console.log("${springMacroRequestContext.contextPath}/rest/positions/delete/" + id);
            document.getElementById('deleteName').textContent = id;
            document.getElementById('confirmDeleteBtn').onclick = "deletePosition("+id+")";
            document.getElementById('link_delete_a').href = "${springMacroRequestContext.contextPath}/positions/delete/" + id;

            // delete action
            //const xhr = new XMLHttpRequest();
            console.log(`Can be deleted for id=` + id);
            const response = await fetch("${springMacroRequestContext.contextPath}/rest/positions/can_delete/" + id);
            const responseText = await response.text();
            console.log("response can_delete:");
            console.log(responseText);
            const answerJson = JSON.parse(responseText);
            console.log("receive answerJson:");
            console.log(answerJson);
            console.log("answerJson.deleteable:");
            console.log(answerJson.deleteable);
            if(answerJson.deleteable == true) {
                console.log("set on click to confirm DELETE");
                console.log("${springMacroRequestContext.contextPath}/positions/delete/" + id);
                const btn = document.getElementById('confirmDeleteBtn');
                btn.onclick = function() {
                    console.log('!!!');
                    console.log("deleting id=" + id);
                    const xhr = new XMLHttpRequest();
                    xhr.open('GET', "${springMacroRequestContext.contextPath}/positions/delete/" + id, false); // синхронный запрос
                    xhr.send();
                    closeDeleteModal();
                };
                // btn.onclick = "deletePosition("+"${springMacroRequestContext.contextPath}/positions/delete/" + id+")";
            } else {
                console.log("can not be_deleted");
            }
            //console.log(`Can be deleted response:` + response);
            //if (response.ok) {
            //    console.log(`Can be deleted response:` + response);
            //    console.log(response);
            //    let json = response.json();
            //} else {
            //    alert("Ошибка HTTP: " + response.status);
            //}
            const modal = document.getElementById('deleteModal');
            modal.style.display = 'flex';
            setTimeout(() => modal.classList.add('show'), 10);
            document.body.style.overflow = 'hidden';
            console.log("answerJson");
            console.log(answerJson);
        }

        function closeDeleteModal() {
            const modal = document.getElementById('deleteModal');
            modal.classList.remove('show');
            setTimeout(() => {
                modal.style.display = 'none';
            }, 300);
            document.body.style.overflow = '';
        }

        function deletePosition(id) {
            // Работает удаление
            console.log("Deleting...");
            console.log(id);
            // console.log("${springMacroRequestContext.contextPath}/rest/positions/delete/" + id);

            const modal = document.getElementById('deleteModal');
            modal.classList.remove('show');
            setTimeout(() => {
               modal.style.display = 'none';
            }, 300);

            // console.log(document.getElementById('confirmDeleteBtn').href);
            // const xhr = new XMLHttpRequest();
            // xhr.open('GET', document.getElementById('confirmDeleteBtn').href, false); // синхронный запрос
            // xhr.send();

            // document.body.style.overflow = '';

            //location.reload();
        }

    </script>
</body>

</html>