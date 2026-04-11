<!DOCTYPE html>
<html lang="en" class="h-full">
<head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <title>Должности - HR Admin</title>

    <!-- Tailwind CSS via CDN -->
    <script src="https://cdn.tailwindcss.com"></script>

    <!-- Inter Font -->
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;500;600;700&display=swap" rel="stylesheet">

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
            @apply px-3 py-1 border border-gray-300 hover:bg-sky-50 hover:text-sky-900 transition font-medium;
        }
        .pagination-current {
            font-weight: 900;
            color: cornflowerblue;
        }
        .pagination-first-last {
            @apply px-3 py-1 border border-gray-300 hover:bg-sky-50 hover:text-sky-900 transition font-bold;
        }
        .sort-icon {
            @apply inline-block text-xs text-gray-400 ml-1 transition-transform duration-200;
        }
        .sort-asc .sort-icon {
            @apply text-sky-700 transform rotate-0;
        }
        .sort-desc .sort-icon {
            @apply text-sky-700 transform rotate-180;
        }
        .py-0.5 {
            padding-top: 0.5rem;
            padding-bottom: 0.5rem;
        }
        .p-09rem {
            padding-top: 0.9rem;
            padding-bottom: 0.9rem;
        }
        .logo-padding {
            padding-top: 0.9em;
            padding-bottom: 0.9em;
        }
        .all-positions {
            padding-top: 0.75em;
            padding-bottom: 0.75em;
            padding-left: 1.5em;
            padding-right: 1.5em;
        }

        /* Стили для модального окна */
        .modal-backdrop {
            @apply fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50;
        }
        .modal-container {
            @apply bg-white rounded-lg shadow-xl p-6 w-full max-w-md mx-4;
        }
        .modal-title {
            @apply text-lg font-semibold text-gray-800 mb-4;
        }
        .modal-actions {
            @apply flex justify-end gap-3 mt-6;
        }
        .btn-cancel {
            @apply px-4 py-2 bg-gray-200 text-gray-800 rounded hover:bg-gray-300 transition;
        }
        .btn-delete {
            @apply px-4 py-2 bg-red-600 text-white rounded hover:bg-red-700 transition;
        }
    </style>
</head>

<body class="bg-gray-50 min-h-screen flex">
    <!-- Include Sidebar Fragment -->
    <#include "fragments/sidebar.ftl">

    <!-- Main Content -->
    <div class="flex flex-1 flex-col overflow-hidden">
        <!-- Header -->
        <header class="bg-sky-900 text-white shadow-sm border-b border-gray-100 all-positions">
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
        </header>

        <!-- Positions List -->
        <main class="flex-1 p-6 overflow-y-auto bg-white">
            <#if positions?has_content>
                <table class="w-full text-sm text-left text-gray-700">
                    <tbody>
                        <#list positions as position>
                        <tr id="position-${position.id}" class="border-b border-gray-100 hover:bg-gray-50 transition">
                            <td class="px-4 py-2 font-medium">${position.name}</td>
                            <td class="px-4 py-2 text-center">
                                <!-- Edit Action -->
                                <a href="${springMacroRequestContext.contextPath}/positions/edit/${position.id}"
                                   class="action-edit inline-flex items-center justify-center w-8 h-8 rounded-full hover:bg-sky-100 transition"
                                   title="Изменить">
                                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-sky-700" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                                        <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M11 5H6a2 2 0 00-2 2v11a2 2 0 002 2h11a2 2 0 002-2v-5m-1.414-9.414a2 2 0 112.828 2.828L11.828 15H9v-2.828l8.586-8.586z" />
                                    </svg>
                                </a>
                                <!-- Delete Action with Confirmation -->
                                <button type="button"
                                        onclick="confirmDelete(${position.id})"
                                        class="inline-flex items-center justify-center w-8 h-8 rounded-full hover:bg-red-100 transition"
                                        title="Удалить">
                                    <svg xmlns="http://www.w3.org/2000/svg" class="h-5 w-5 text-red-600" fill="none" viewBox="0 0 24 24" stroke="currentColor">
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
    <script th:inline="javascript">
            function confirmDelete(position_id) {
                console.log('Confirm delete:' + position_id);
                console.log('${springMacroRequestContext.contextPath}/positions/' + position_id);
                var xhttp = new XMLHttpRequest();
                xhttp.open("DELETE", '${springMacroRequestContext.contextPath}/positions/delete/' + position_id, true);
                xhttp.onload = function () {
                   let responseURL = '${springMacroRequestContext.contextPath}/positions/';
                   console.log("Redirecting to:", responseURL);
                   window.location.replace(responseURL);
                };
                xhttp.send();
            }

            function closeModal() {
                console.log('closeModal');
            }
        </script>
</body>
</html>