<!DOCTYPE html>
<html lang="en" class="h-full">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Employees - HR Admin</title>

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
    <!-- Show employees page -->
    <!-- Include Sidebar Fragment -->
    <#include "fragments/sidebar.ftl">
    <!-- End Include Sidebar Fragment -->

    <!-- Main Content -->
    <div class="flex flex-1 flex-col overflow-hidden">
        <!-- Header -->
        <div class="w-full mx-auto bg-white shadow-xl border border-gray-100">
            <div class="bg-sky-900 px-6 py-1 text-white text-center">
                <h2 class="text-2xl font-semibold">Список сотрудников</h2>
                <p class="text-sky-200">Страница ${currentPage + 1} из ${totalPages}, всего: ${totalElements} записей</p>
            </div>
            <!-- Caption -->
            <div class="p-6 overflow-x-auto">
                <#if employees?has_content> <!-- IF employees?has_content -->
                    <table class="w-full text-sm text-left text-gray-700 table-auto">
                        <thead class="uppercase border-t border-gray-100 bg-gray-50">
                            <!-- First Name -->
                            <th class="px-6 p-header cursor-pointer group font-medium font-black"
                                onclick="window.location.href='?page=${currentPage}&sortField=firstName&direction=${(sortField == 'firstName' && direction = 'asc')?string('desc','asc')}'">
                                Имя
                                <span class="text-gray-800 text-2xl">
                                        <#if sortField == "firstName">
                                            <#if direction == "asc">↓</#if>
                                            <#if direction == "desc">↑</#if>
                                        </#if>
                                </span>
                            </th>
                            <!-- Last Name -->
                            <th class="px-6 p-top-header cursor-pointer group font-medium font-black"
                                onclick="window.location.href='?page=${currentPage}&sortField=lastName&direction=${(sortField == 'lastName' && direction = 'asc')?string('desc','asc')}'">Фамилия
                                <span class="text-gray-800 text-2xl">
                                        <#if sortField == "lastName">
                                            <#if direction == "asc">↓</#if>
                                            <#if direction == "desc">↑</#if>
                                        </#if>
                                </span>
                            </th>
                            <!-- E-mail -->
                            <th class="px-6 p-top-header cursor-pointer group font-medium font-black"
                                onclick="window.location.href='?page=${currentPage}&sortField=email&direction=${(sortField == 'email' && direction == 'asc')?string('desc','asc')}'">Email
                                <span class="text-gray-800 text-2xl">
                                  <#if sortField == "email">
                                    <#if direction == "asc">↓</#if>
                                    <#if direction == "desc">↑</#if>
                                  </#if>
                                </span>
                            </th>
                            <!-- Position -->
                            <th class="px-6 p-top-header cursor-pointer group font-medium font-black"
                                onclick="window.location.href='?page=${currentPage}&sortField=position<#if direction == 'desc'>&direction=asc<#else>&direction=desc</#if>'">Должность
                                <span class="text-gray-800 text-2xl">
                                   <#if sortField == "position.name">
                                      <#if direction == "asc">↓</#if>
                                      <#if direction == "desc">↑</#if>
                                    </#if>
                                </span>
                            </th>
                        </thead>
                        <tbody>
                            <!-- Employee Details -->
                            <#list employees as empl>
                                <tr class="border-b border-gray-100 hover:bg-gray-50 transition">
                                    <td class="px-6 py-4 font-medium">${empl.firstName}</td>
                                    <td class="px-6 py-4">${empl.lastName}</td>
                                    <td class="px-6 py-4">${empl.email}</td>
                                    <td class="px-6 py-4">${empl.position.name}</td>
                                    <td class="px-6 py-4 flex justify-center gap-4">
                                        <a href="${springMacroRequestContext.contextPath}/employees/edit/${empl.id}"
                                           class="action-edit inline-flex items-center justify-center w-8 h-8 rounded-full hover:bg-sky-100 transition"
                                           title="Изменить">
                                            <svg class="action-icon" xmlns="https://www.w3.org/2000/svg" fill="none" viewBox="-4 -4 30 30" stroke="currentColor">
                                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z"/>
                                            </svg>
                                        </a>

                                        <!-- Delete Employee with Confirmation -->
                                        <button type="button"
                                                onclick="openDeleteModal('${springMacroRequestContext.contextPath}/employees/delete/${empl.id}', '${empl.lastName}')"
                                                title="Удалить сотрудника"
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
        <!-- Pagination -->
        <div class="flex justify-between items-center mt-6">
            <div class="text-sm text-gray-600">
                Показано ${(currentPage * 10) + 1}–${(currentPage * 10) + employees?size} из ${totalElements}
            </div>

            <ul class="inline-flex space-x-1">
                <!-- Previous Button -->
                <#if currentPage gt 0>
                <li>
                    <a href="${springMacroRequestContext.contextPath}/employees/show_employees?page=${currentPage - 1}&sortField=${sortField}&direction=${direction}" class="pagination-link">
                        <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
                            <title>Предыдущая страница</title>
                            <path fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="m11 5l-7 7l7 7m-7-7h16"/>
                        </svg>
                    </a>
                </li>
                <#else>
                <li>
                                <span class="pagination-link opacity-50 cursor-not-allowed">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
                                        <title>Предыдущая страница</title>
                                        <path fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="m11 5l-7 7l7 7m-7-7h16"/>
                                    </svg>
                                </span>
                </li>
            </#if>

            <!-- Page Numbers -->
            <#assign startPage = [currentPage - 2, 0]?max />
            <#assign endPage = [(startPage + 4), totalPages - 1]?min />

            <#list startPage..endPage as p>
            <li>
                <#if p == currentPage>
                <span class="pagination-current">${p + 1}</span>
                <#else>
                <a href="${springMacroRequestContext.contextPath}/employees/show_employees?page=${p}&sortField=${sortField}&direction=${direction}" class="pagination-link">${p + 1}</a>
            </#if>
            </li>
        </#list>

        <!-- Next Button -->
        <#if currentPage lt totalPages - 1>
        <li>
            <a href="${springMacroRequestContext.contextPath}/employees/show_employees?page=${currentPage + 1}&sortField=${sortField}&direction=${direction}" class="pagination-link">
                <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
                    <title>Следующая страница</title>
                    <path fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 12h16m-7-7l7 7l-7 7"/>
                </svg>
            </a>
        </li>
        <#else>
        <li>
                                <span class="pagination-link opacity-50 cursor-not-allowed">
                                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
                                        <title>Следующая страница</title>
                                        <path fill="none" stroke="currentColor" stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M4 12h16m-7-7l7 7l-7 7"/>
                                    </svg>
                                </span>
        </li>
    </#if>
    </ul>
    </div>
    <!-- End Pagination -->
                <#else>
                    <p class="text-center py-6 text-gray-500">Сотрудники не найдены.</p>
                </#if>                 <!-- IF employees?has_content -->
            </div> <!-- Caption -->
        </div> <!-- Header -->
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
            <button id="confirmDeleteBtn" type="button" onclick="deleteEmployee()" class="btn-delete">Удалить</button>
        </div>
    </div>
</div>
<script>
    window.onclick = function(event) {
        const modal = document.getElementById('confirmDeleteBtn');
        console.log(`Clicked confirmDeleteBtn`);

        if (event.target === modal && modal.classList.contains('show')) {
            closeDeleteModal();
        }
    }

    function openDeleteModal(url, name) {
        document.getElementById('confirmDeleteBtn').href = url;
        document.getElementById('deleteName').textContent = name;
        const modal = document.getElementById('deleteModal');
        modal.style.display = 'flex';
        setTimeout(() => modal.classList.add('show'), 10);
        document.body.style.overflow = 'hidden';
    }

    function closeDeleteModal() {
        const modal = document.getElementById('deleteModal');
        modal.classList.remove('show');
        setTimeout(() => {
            modal.style.display = 'none';
        }, 300);
        document.body.style.overflow = '';
    }

    function deleteEmployee() {
        console.log("Deleting...");
        const modal = document.getElementById('deleteModal');
        modal.classList.remove('show');
        setTimeout(() => {
            modal.style.display = 'none';
        }, 300);

        console.log(document.getElementById('confirmDeleteBtn').href);
        const xhr = new XMLHttpRequest();
        xhr.open('GET', document.getElementById('confirmDeleteBtn').href, false); // синхронный запрос
        xhr.send();

        document.body.style.overflow = '';
        location.reload();
    }

</script>
</body>

</html>