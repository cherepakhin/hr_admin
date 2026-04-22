<!DOCTYPE html>
<html lang="en" class="h-full">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Employees - HR Admin</title>

    <!-- Favicon -->
    <link rel="icon" href="/hr_admin/favicon.ico" type="image/x-icon">
    <link rel="shortcut icon" href="/hr_admin/favicon.ico" type="image/x-icon">

    <!--<script src="https://cdn.tailwindcss.com"></script>-->
    <script src="${springMacroRequestContext.contextPath}/js/tailwind.js"></script>

    <!-- Alpine.js for interactivity -->
    <!--<script src="https://unpkg.com/alpinejs@3.x.x/dist/cdn.min.js" defer></script>-->
    <!--<script src="${springMacroRequestContext.contextPath}/js/cdn.min.js" defer></script>-->

    <!-- Inter Font -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">

    <link href="${springMacroRequestContext.contextPath}/css/hr_admin.css" rel="stylesheet">

    <style>
        body {
            font-family: 'Inter', sans-serif;
        }
        .btn-dark-blue {
            @apply bg-sky-900 hover:bg-sky-800 text-white;
        }
        .sidebar-link:hover, .sidebar-link.active {
            @apply bg-gray-100 text-sky-900;
        }
        .sidebar-link svg {
            @apply transition-colors duration-200;
        }
        .sidebar-link:hover svg, .sidebar-link.active svg {
            @apply text-sky-900;
        }
        .pagination-link {
            @apply px-3 py-1 border border-gray-300 rounded hover:bg-sky-50 hover:text-sky-900 transition font-medium;
        }
        .pagination-current {
            font-weight: 900;
            color: cornflowerblue;
        }
        .all-employees {
            padding-top: 0.6em;
            padding-bottom: 0.6em;
            padding-left: 1.5em;
            padding-right: 1.5em;
        }

        /* Мобильная адаптация */
        @media (max-width: 640px) {
            .mobile-header {
                flex-direction: column;
                align-items: flex-start;
                gap: 0.5rem;
            }
            .mobile-sort-container {
                flex-direction: column;
                align-items: stretch;
                width: 100%;
                margin-top: 0.5rem;
            }
            .mobile-full-width {
                width: 100% !important;
            }
            .w-40 {
                width: 100%;
            }
            .flex\.justify-between\.items-right {
                flex-direction: column;
                align-items: stretch;
            }
            #sortSelectHeader {
                margin-bottom: 0.5rem;
            }
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
    <!--Index page -->
    <!-- Include Sidebar Fragment -->
    <#include "fragments/sidebar.ftl">
    <!-- End Include Sidebar Fragment -->

    <!-- Main Content -->
    <div class="flex-1 flex flex-col overflow-hidden">

        <!-- Header -->
        <header class="bg-white shadow-sm border-b border-gray-100 all-employees">
            <div class="flex mobile-header justify-between items-center">
                <h2 class="font-semibold text-gray-800 px-2" style="font-size: 1.5rem; line-height: 1.75rem;">Все сотрудники</h2>
                <div class="flex mobile-sort-container justify-between items-center gap-2">
                    <a href="${springMacroRequestContext.contextPath}/employees/new"
                       class="btn-dark-blue mobile-add-btn px-5 py-2 font-medium shadow transition flex items-center gap-2 rounded-none hover:bg-sky-50 hover:text-sky-900 w-40">
                        <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                            <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M12 6v6m0 0v6m0-6h6m-6 0H6" />
                        </svg>
                        Добавить
                    </a>
                    <h3 id="sortSelectHeader" class="text-1xl py-2 px-4 font-semibold text-gray-800 mb-0">Сортировать по:</h3>
                    <!-- Combobox для сортировки с поддержкой выбранного значения -->
                    <div id="sortSelectContainer" class="relative w-40 mobile-full-width">
                        <select
                            id="sortSelect"
                            onchange="window.location.href='?sortField=' + this.value + '&direction=asc'"
                            class="block w-full px-5 py-2 bg-white border border-gray-300 shadow-sm focus:outline-none focus:ring-2 focus:ring-sky-500 focus:border-sky-500 appearance-none rounded-none">
                            <option value="firstName" <#if sortField?? && sortField == "firstName">selected</#if>>Имени</option>
                            <option value="lastName" <#if sortField?? && sortField == "lastName" || !sortField??>selected</#if>>Фамилии</option>
                            <option value="email" <#if sortField?? && sortField == "email">selected</#if>>Email</option>
                            <option value="position.name" <#if sortField?? && sortField == "position.name">selected</#if>>Должности</option>
                        </select>
                        <!-- Кастомная стрелка -->
                        <div class="absolute inset-y-0 right-0 flex items-center px-2 pointer-events-none">
                            <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-gray-400" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 9l-7 7-7-7" />
                            </svg>
                        </div>
                    </div>
                </div>
            </div>
        </header>

        <!-- Employee List -->
        <main class="flex-1 p-6 overflow-y-auto">
            <#if employees?has_content>
                <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6 mb-6">
                    <#list employees as employee>
                    <div class="bg-white p-6 shadow-md border border-gray-100 transition-all duration-200">
                        <div class="flex items-center mb-4">
                            <div class="w-12 h-12 bg-gradient-to-r from-sky-700 to-sky-900 flex items-center justify-center text-white font-bold text-lg">
                                ${employee.firstName[0]}
                            </div>
                            <div class="ml-4">
                                <h3 class="text-lg font-semibold text-gray-800 hover:bg-gray-100 employees-list-item-padding">
                                    <a href="${springMacroRequestContext.contextPath}/employees/edit/${employee.id}">
                                    ${employee.firstName} ${employee.lastName}</a>
                                </h3>
                                <p class="text-sm text-gray-500">${employee.position.name}</p>
                                <p class="text-sm text-gray-500">${employee.email}</p>
                            </div>
                        </div>
                        <div class="flex justify-end gap-2">
                            <!-- Edit Button -->
                            <!-- gap-1 расстояние между иконкой и текстом --->
                            <a href="${springMacroRequestContext.contextPath}/employees/edit/${employee.id}"
                               class="text-sky-900 hover:text-sky-700 hover:bg-gray-100 font-medium text-sm transition flex items-center gap-1">
                               <svg xmlns="http://www.w3.org/2000/svg" class="h-4 w-4" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                  <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                               </svg>
                               Изменить
                            </a>
                            <!-- Delete Employee with Confirmation -->
                            <button type="button"
                                    onclick="openDeleteModal('${springMacroRequestContext.contextPath}/employees/delete/${employee.id}', '${employee.lastName}')"
                                title="Удалить сотрудника"
                                class="text-sky-900 hover:text-sky-700 hover:bg-red-100 font-medium text-sm transition flex items-center gap-1">
                                <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-red-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M19 7l-.867 12.142A2 2 0 0116.138 21H7.862a2 2 0 01-1.995-1.858L5 7m5 4v6m4-6v6m1-10V4a1 1 0 00-1-1h-4a1 1 0 00-1 1v3M4 7h16" />
                                </svg>
                               Удалить
                            </button>
                        </div>
                    </div>
                    </#list>
                </div>

                <!-- Pagination -->
                <div class="flex justify-between items-center mt-6">
                    <div class="text-sm text-gray-600">
                        Показано ${(currentPage * 10) + 1}–${(currentPage * 10) + employees?size} из ${totalElements}
                    </div>

                    <ul class="inline-flex space-x-1">
                        <!-- Previous Button -->
                        <#if currentPage gt 0>
                            <li>
                                <a href="${springMacroRequestContext.contextPath}/employees/?page=${currentPage - 1}&sortField=${sortField}&direction=${direction}" class="pagination-link">
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
                                    <a href="${springMacroRequestContext.contextPath}/employees/?page=${p}&sortField=${sortField}&direction=${direction}" class="pagination-link">${p + 1}</a>
                                </#if>
                            </li>
                        </#list>

                        <!-- Next Button -->
                        <#if currentPage lt totalPages - 1>
                            <li>
                                <a href="${springMacroRequestContext.contextPath}/employees/?page=${currentPage + 1}&sortField=${sortField}&direction=${direction}" class="pagination-link">
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
                <div class="bg-white p-10 shadow text-center border border-gray-100">
                    <svg xmlns="http://www.w3.org/2000/svg" class="mx-auto h-12 w-12 text-sky-300" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
                    </svg>
                    <h3 class="mt-4 text-lg font-medium text-gray-700">Сотрудники не найдены</h3>
                    <p class="text-gray-500 mt-1">Добавьте первого сотрудника.</p>
                    <a href="${springMacroRequestContext.contextPath}/employees/new"
                       class="btn-dark-blue inline-flex items-center px-4 py-2 text-sm font-medium shadow transition mt-4">
                        Добавить сотрудника
                    </a>
                </div>
            </#if>
        </main>
    </div>

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

        window.onclick = function(event) {
            const modal = document.getElementById('confirmDeleteBtn');
            console.log(`Clicked confirmDeleteBtn`);

            if (event.target === modal && modal.classList.contains('show')) {
                closeDeleteModal();
            }
        }
    </script>
</body>
</html>