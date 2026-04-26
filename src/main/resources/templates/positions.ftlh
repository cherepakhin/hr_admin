<!DOCTYPE html>
<html lang="en" class="h-full">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Должности - HR Admin</title>

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
        .btn-dark-blue {
            background-color: #0a3d62;
            color: white;
            padding: 0.5rem 1rem;
            border: none;
            border-radius: 0;
            font-weight: 500;
            box-shadow: 0 1px 3px rgba(0,0,0,0.1);
            transition: background-color 0.2s;
        }
        .btn-dark-blue:hover {
            background-color: #0a3d62;
            color: white;
        }
        .sidebar-link:hover, .sidebar-link.active {
            background-color: #f1f5f9;
            color: #0a3d62;
        }
        .sidebar-link svg {
            transition: color 0.2s;
        }
        .all-positions {
            padding-top: 0.75em;
            padding-bottom: 0.75em;
            padding-left: 1.5em;
            padding-right: 1.5em;
        }

        /* Стили для модального окна Удаления */
        #deleteModal {
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
        #deleteModal.show .modal-container {
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
    <!-- Include Sidebar Fragment -->
    <#include "fragments/sidebar.ftl">

    <!-- Main Content -->
    <div class="flex-1 flex flex-col overflow-hidden">
        <!-- Header -->
        <div class="bg-sky-900 px-6 logo-padding text-white text-center py-4">
            <div class="flex justify-between items-center">
                <h2 class="text-2xl font-semibold text-white">Должности</h2>
                <a href="${springMacroRequestContext.contextPath}/positions/new"
                   class="btn-dark-blue px-5 py-2 font-medium shadow transition flex items-center gap-2 rounded-none hover:bg-sky-50 hover:text-sky-900 border-l-2 border-transparent hover:border-sky-900">
                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
                    </svg>
                    Добавить
                </a>
            </div>
        </div>

        <!-- Positions List -->
        <main class="flex-1 p-6 overflow-y-auto bg-white">
            <#if positions?has_content>
                <table class="w-full text-sm text-left text-gray-700">
                    <tbody>
                        <#list positions as position>
                            <tr id="position-${position.id}" class="border-b border-gray-100 hover:bg-gray-50 transition">
                                <td class="px-4 py-2 font-medium">${position.name}</td>
                                <td class="px-4 py-2 text-right">
                                    <!-- Edit Action -->
                                    <a href="${springMacroRequestContext.contextPath}/positions/edit/${position.id}"
                                       class="action-edit inline-flex items-center justify-center w-8 h-8 rounded-full hover:bg-sky-100 transition"
                                       title="Изменить">
                                       <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-sky-700" fill="none"
                                          viewBox="0 0 24 24" stroke="currentColor">
                                          <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                                       </svg>
                                    </a>
                                    <!-- Delete Action with Custom Modal -->
                                    <button type="button"
                                        onclick="openDeleteModal('${position.id}', '${position.name}')"
                                        class="inline-flex items-center justify-center w-8 h-8 rounded-full hover:bg-red-100 transition text-red-600"
                                        title="Удалить">
                                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                                        </svg>
                                    </button>
                                </td>
                            </tr>
                        </#list>
                    </tbody>
                </table>
            <#else>
                <p class="text-center py-6 text-gray-500">Должности не найдены.</p>
            </#if>
        </main>
</div>

<!-- Confirmation Delete Modal Dialog -->
<div id="idDeleteModalDlg" class="fixed inset-0 hidden">
    <div class="modal-container">
        <div class="modal-title">
            <svg class="warning-icon" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 9v2m0 4h.01m-6.938 4h13.856c1.54 0 2.502-1.667 1.732-3L13.732 4c-.77-1.333-2.694-1.333-3.464 0L3.34 16c-.77 1.333.192 3 1.732 3z" />
            </svg>
            Подтверждение удаления
        </div>
        <p>Вы уверены, что хотите удалить должность "<span id="deleteName" class="font-medium"></span>"?</p>
        <div class="modal-actions">
            <button type="button" onclick="closeDeleteModal()" class="btn-cancel">Отмена</button>
            <a id="confirmDeleteBtn" href="#" class="btn-delete">Удалить</a>
        </div>
    </div>
</div>

<script>
    function openDeleteModal(id_position, name) {
        // '${springMacroRequestContext.contextPath}/positions/delete/',
        // fetch().then(function(response) {
        //    return response.json();
        // }).then(function(data) {
        //    console.log(data);
        // }).catch(function(err) {
        //    console.log('Fetch Error :-S', err);
        // });

        // Заполняю ссылку для кнопки потвердждния удаления вместо href=#
        // НО! нужно проверить можно ли это сделать в контроллере (не назначена ли должность сотрудникам)
        document.getElementById('confirmDeleteBtn').href = '${springMacroRequestContext.contextPath}/positions/delete/' + id_position;
        document.getElementById('deleteName').textContent = name;
        const deleteModalDlg = document.getElementById('idDeleteModalDlg');
        deleteModalDlg.style.display = 'flex';
        // ОТКРЫТИЕ диалога с заданной задержкой делается добавлением класса 'show'
        // setTimeout(() => modal.classList.add('show'), 1);

        // или без задережки
        deleteModalDlg.classList.add('show');
        // задний план скрыт (становится серым)
        document.body.style.overflow = 'hidden';
    }

    function closeDeleteModal() {
        const modal = document.getElementById('deleteModal');

        modal.classList.remove('show');

        // скрытие диалогового окна  через удаление класса 'show' с заданной задержкой 1
        //setTimeout(() => {
        //    modal.style.display = 'none';
        //}, 1);

        // Скрытие без задержки
        modal.style.display = 'none';
        // активация заднего плана (становится не серым)
        document.body.style.overflow = '';
    }

    window.onclick = function(event) {
        const modal = document.getElementById('deleteModal');
        if (event.target === modal && modal.classList.contains('show')) {
            closeDeleteModal();
        }
    }
</script>
</body>
</html>